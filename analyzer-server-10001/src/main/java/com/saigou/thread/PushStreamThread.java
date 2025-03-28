package com.saigou.thread;

import com.saigou.draw.Draw;
import com.saigou.entity.FrameWrapper;
import com.saigou.grpc.FaceBox;
import com.saigou.grpc.PersonBox;
import com.saigou.grpc.Point;
import com.saigou.properties.PushProperties;
import com.saigou.util.Utils;
import lombok.SneakyThrows;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class PushStreamThread extends Thread{
    private static final Logger log = LoggerFactory.getLogger(PushStreamThread.class);
    public FFmpegFrameGrabber grabber;
    public FFmpegFrameRecorder recorder;
    public LinkedBlockingQueue<Frame> pushFrameQueue;
    public ConcurrentSkipListMap<Long, FrameWrapper> resultCache;
    public CopyOnWriteArrayList<Long> keyList;
    public boolean isAlive = false;

    @SneakyThrows
    public PushStreamThread(String url, FFmpegFrameGrabber grabber, LinkedBlockingQueue<Frame> pushFrameQueue,
                            ConcurrentSkipListMap<Long, FrameWrapper> resultCache,
                            CopyOnWriteArrayList<Long> keyList, PushProperties pushProperties) {
        this.resultCache = resultCache;
        this.grabber = grabber;
        this.pushFrameQueue = pushFrameQueue;
        this.keyList = keyList;
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
        recorder.setGopSize(60); // 关键帧间隔（帧数）
        recorder.setVideoOption("tune", pushProperties.getTune()); // 零延迟模式
        recorder.setVideoOption("crf",pushProperties.getCrf()); // 画质与码率平衡
        recorder.setVideoOption("preset", pushProperties.getPreset());     // 预设模式
        recorder.setVideoOption("quality", pushProperties.getQuality());        // 速度优先
        recorder.setVideoOption("rc", pushProperties.getRc());         // 低延迟码率控制
        recorder.setVideoOption("usage", pushProperties.getUsage());// 超低延迟模式
        recorder.setVideoOption("threads", pushProperties.getThreads());
        recorder.setOption("rtbufsize", pushProperties.getRtbufsize());     // 实时缓冲区大小
        recorder.setOption("max_delay",pushProperties.getMax_delay());    // 最大延迟（微秒）
        recorder.setOption("reconnect", pushProperties.getReconnect());         // 启用自动重连
        recorder.setOption("reconnect_at_eof", pushProperties.getReconnect_at_eof());  // 在EOF时重连
        recorder.setOption("reconnect_streamed", pushProperties.getReconnect_streamed());// 流式重连
        recorder.start();
        // 在程序初始化时启用 OpenCL
        opencv_core.setUseOpenCL(true);
    }



    public OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    public void run(){
        isAlive = true;
        final long MAX_WAIT_MS = (long) ((1000/recorder.getFrameRate())-10-5);// 最大等待结果时间
        long startTime = System.currentTimeMillis();
        int frameCount = 0;
        long oldtimestamp=-1;
        List<FaceBox> faceBoxList= null;
        List<PersonBox> personBoxList= null;
        try{
            while (resultCache.isEmpty()){
                Thread.sleep(20); // 避免持续轮询，减少 CPU 占用
            }
            while (!isInterrupted()) {
                Frame frame = pushFrameQueue.poll(5, TimeUnit.MILLISECONDS);
                if(frame != null && frame.timestamp <= oldtimestamp){
                    System.out.println("时间戳回退,丢弃帧："+oldtimestamp+"->"+frame.timestamp);
                }
                if (frame != null && frame.image != null && frame.timestamp > oldtimestamp){
                    long startWait = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startWait < MAX_WAIT_MS) {
                        FrameWrapper result = resultCache.get(frame.timestamp);
                        if (result != null) break;
                    }
                    FrameWrapper result = CacheFrameHandler(frame.timestamp);
                    if (result != null) {
                        faceBoxList = result.faceBoxes;
                        personBoxList = result.PersonBoxs;
                        recorder.record(result.frame);
                        oldtimestamp=result.frame.timestamp;
                        Utils.safeCloseFrame(result.frame);
                    } else {
                        if(faceBoxList!=null||personBoxList!=null){// 用上一次分析结果绘制人脸框和人体关键点
                            Mat mat = converter.convert(frame);
                            if(faceBoxList!=null){
                                for (FaceBox faceBox : faceBoxList) {
                                    Draw.drawRectangle(mat, faceBox.getMinPoint(), faceBox.getMaxPoint());
                                    Draw.drawText(mat, faceBox.getExpressionFeature(), faceBox.getMinPoint());
                                }
                            }
                            if(personBoxList!=null){
                                for (PersonBox personBox : personBoxList) {
                                    List<Point> points = personBox.getPointsList();
                                    Draw.drawPersonPose(mat, points);
                                }
                            }
                            frame = converter.convert(mat);
                        }
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
                    Utils.safeCloseFrame(frame);
                }
            }
        }catch (Exception e){
//            log.error("推流异常",e);
        }finally {
            isAlive = false;
        }
    }

    private FrameWrapper CacheFrameHandler(long timestamp) throws InterruptedException {
        Long key;
        Iterator<Long> list = keyList.iterator();
        while (list.hasNext()){
            key = list.next();
            if (key==timestamp){
                return resultCache.remove(timestamp);
            }
            if (key<timestamp){
                FrameWrapper remove = resultCache.remove(key);
                if (remove != null) {
                    keyList.remove(key);
                    Utils.safeCloseFrame(remove.frame);
                    System.out.println("[缓存帧超时:丢弃]："+key);
                }
            }else{
                break;
            }
        }
        return null;
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