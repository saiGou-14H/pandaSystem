package com.saigou.thread;

import com.saigou.grpc.AnalysisResult;
import lombok.SneakyThrows;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PushStreamThread extends Thread{
    public FFmpegFrameGrabber grabber;
    public FFmpegFrameRecorder recorder;
    public LinkedBlockingQueue<Frame> pushFrameQueue;
    public ConcurrentSkipListMap<Long, Frame> resultCache;
    // 新增流量控制属性
    private final Semaphore semaphore = new Semaphore(30);  // 限制每秒最多30帧



    @SneakyThrows
    public PushStreamThread(String url, FFmpegFrameGrabber grabber, LinkedBlockingQueue<Frame> pushFrameQueue, ConcurrentSkipListMap<Long, Frame> resultCache) {
        this.resultCache = resultCache;
        this.grabber = grabber;
        this.pushFrameQueue = pushFrameQueue;
        // 2. 推流初始化
        recorder = new FFmpegFrameRecorder(
                url,
                grabber.getImageWidth(),
                grabber.getImageHeight()
        );

        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoBitrate(grabber.getVideoBitrate()); // 码率与输入一致 (10_000_000 10Mbs)
        recorder.setFormat("flv");
        recorder.setVideoOption("tune", "zerolatency"); // 零延迟模式
        recorder.setVideoOption("crf", "23"); // 画质与码率平衡
        recorder.setGopSize(60); // 关键帧间隔（帧数）
        // AMF 硬件编码专用参数
        recorder.setVideoOption("preset", "ultrafast");     // 预设模式
        recorder.setVideoOption("quality", "speed");        // 速度优先
        recorder.setVideoCodecName("h264_amf");// AMD
        recorder.setVideoOption("rc", "cbr_ld_hq");         // 低延迟码率控制
        recorder.setVideoOption("usage", "ultralowlatency");// 超低延迟模式
        // 限制线程数（AMF对多线程支持有限）
        recorder.setVideoOption("threads", "8");

        recorder.setOption("rtbufsize", "2000k");     // 实时缓冲区大小
        recorder.setOption("max_delay", "500000");    // 最大延迟（微秒）
        recorder.setOption("reconnect", "1");         // 启用自动重连
        recorder.setOption("reconnect_at_eof", "1");  // 在EOF时重连
        recorder.setOption("reconnect_streamed", "1");// 流式重连

//        recorder.setVideoCodecName("h264_nvenc"); // NVIDIA
//        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
//        recorder.setVideoOption("pix_fmt", "yuv420p");      // 双重保险
        recorder.start();

        // 在程序初始化时启用 OpenCL
        opencv_core.setUseOpenCL(true);
        // 验证加速是否生效
        System.out.println("OpenCL 启用状态: " + opencv_core.useOpenCL());
    }




    @SneakyThrows
    public void run(){

        final long MAX_WAIT_MS = (long) (1000/recorder.getFrameRate()-5); // 最大等待结果时间
        System.out.println("最大延迟："+MAX_WAIT_MS+"ms");
        long startTime = System.currentTimeMillis();
        int frameCount = 0;

        while (resultCache.isEmpty()){
            Thread.sleep(20); // 避免持续轮询，减少 CPU 占用
        }
        long oldtimestamp=0;
        while (!isInterrupted()) {
            Frame frame = pushFrameQueue.poll(5, TimeUnit.MILLISECONDS);
            if (frame != null && frame.image != null && frame.timestamp > oldtimestamp){
                //等待对应分析结果
                long startWait = System.currentTimeMillis();
                while (System.currentTimeMillis() - startWait < MAX_WAIT_MS) {
                    Frame result = resultCache.get(frame.timestamp);
                    if (result != null) break;
                    Thread.sleep(5); // 避免持续轮询，减少 CPU 占用
                }
                // 获取并处理结果
                Frame result = resultCache.remove(frame.timestamp);
                if (result != null) {
                    recorder.record(result);
                    oldtimestamp=result.timestamp;
                    result.close();
                } else {
                    recorder.record(frame);
                    oldtimestamp=frame.timestamp;
                }

                frameCount++;
                // 每 5 秒输出一次帧率
                if (System.currentTimeMillis() - startTime > 5000) {
                    double fps = frameCount / 5.0;
                    System.out.printf("实际推流帧率: %.2f FPS\n", fps);
                    frameCount = 0;
                    startTime = System.currentTimeMillis();
                }
                if(frame.timestamp < oldtimestamp){
                    System.out.println("时间戳回退,丢弃帧："+oldtimestamp+"->"+frame.timestamp);
                }
                frame.clone();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        if (recorder != null) {
            try {
                recorder.stop();    // 停止录制并写入尾部
                recorder.release(); // 释放本地内存
                recorder.close();   // 关闭输出流
            } catch (FrameRecorder.Exception e) {
                throw new RuntimeException("停止或释放录制资源时出错", e);
            }
        }
        System.out.println("推流结束");
    }
}