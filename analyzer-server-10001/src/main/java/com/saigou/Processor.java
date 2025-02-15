package com.saigou;

import lombok.SneakyThrows;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class PullThread extends Thread{
    public String url;
    public FFmpegFrameGrabber grabber;
    public Queue<Frame> framesqueue;

    @SneakyThrows
    public PullThread(String url, Queue<Frame> framesqueue) {
        this.url = url;
        this.framesqueue = framesqueue;
        this.grabber = new FFmpegFrameGrabber(this.url);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();
    }

    @SneakyThrows
    public void run(){
        int width = grabber.getImageWidth();
        int height =grabber.getImageHeight();

        Frame frame;
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image;
        Graphics2D g2d;
        while (!Thread.currentThread().isInterrupted() && ((frame = grabber.grab()) != null)) {
            if (frame.image != null) {
                // 推流
                // 处理帧（添加水印）
                image = converter.getBufferedImage(frame);
                g2d = image.createGraphics();
                g2d.setColor(Color.red);
                g2d.setFont(new Font("Arial", Font.BOLD, 100));
                g2d.drawString("Live", width/2, height/2);
                g2d.dispose();
                Frame processedFrame = converter.convert(image);
                framesqueue.offer(processedFrame);

            }
            //if (frame.samples != null) {
            //     处理音频
            //    framesqueue.offer(frame);
            //}
        }
        converter.close();
    }
    @Override
    public void interrupt(){
        super.interrupt();
        if (grabber != null) {
            try {
                grabber.stop();
                grabber.release();
                grabber.close();
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("拉流结束");
    }
}
class PushThread extends Thread{
    public FFmpegFrameGrabber grabber;
    public FFmpegFrameRecorder recorder;
    public Queue<Frame> framesqueue;

    @SneakyThrows
    public PushThread(String url, FFmpegFrameGrabber grabber,Queue framesqueue) {
        this.grabber = grabber;
        this.framesqueue = framesqueue;
        // 2. 推流初始化
        recorder = new FFmpegFrameRecorder(
                url,
                grabber.getImageWidth(),
                grabber.getImageHeight()
//                     grabber.getAudioChannels()
        );
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoBitrate(grabber.getVideoBitrate()); // 码率与输入一致
        recorder.setVideoOption("preset", "ultrafast"); // 超快编码，降低延迟
        recorder.setVideoOption("tune", "zerolatency"); // 零延迟模式
        recorder.setVideoOption("crf", "23"); // 画质与码率平衡
        recorder.setGopSize(60); // 关键帧间隔（帧数）

        // 仅在存在音频时设置音频参数
//            if (grabber.getAudioChannels() > 0) {
//                recorder.setAudioChannels(grabber.getAudioChannels());
//                recorder.setAudioCodec(grabber.getAudioCodec());
//                recorder.setAudioBitrate(grabber.getAudioBitrate());
//                recorder.setSampleRate(grabber.getSampleRate());
//                // 设置音频参数
//                recorder.setAudioOption("crf", "0");
//                System.out.println("音频参数设置成功");
//            }

        recorder.start();
    }

    @SneakyThrows
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
            if (framesqueue.isEmpty()) {
                continue;
            }
            Frame frame = framesqueue.poll();
            // 检查 frame 是否有效
            if (frame.image != null || frame.samples != null) {
                // 记录帧
                recorder.setTimestamp(frame.timestamp);
                recorder.record(frame);
            }
        }
    }
    @Override
    public void interrupt(){
        super.interrupt();
        if (recorder != null) {
            try {
                recorder.stop();    // 1. 停止录制并写入尾部
                recorder.release(); // 2. 释放本地内存
                recorder.close();   // 3. 关闭输出流
            } catch (FrameRecorder.Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("推流结束");
    }
}
class videoStreamProcessor {
    private Queue<Frame> framesqueue = new ConcurrentLinkedQueue<>();
    PullThread pullThread;
    PushThread pushThread;
    videoStreamProcessor(String pullUrl, String pushUrl) {
        this.pullThread = new PullThread(pullUrl,framesqueue);
        this.pushThread = new PushThread(pushUrl, pullThread.grabber,framesqueue);
    }
    public void start() {
        pullThread.start();
        pushThread.start();
    }

    public void stop() {
        pushThread.interrupt();
        pullThread.interrupt();
        framesqueue.clear();
    }

}
public class Processor {
    static {
        // 启用FFmpeg详细日志
        FFmpegLogCallback.set();
    }


    public static void main(String[] args) throws InterruptedException {
        String url1 = "rtsp://127.0.0.1:7554/live/test2";
        String url2 = "rtmp://127.0.0.1:7935/live/test3";
        videoStreamProcessor videoStreamProcessor = new videoStreamProcessor(url1, url2);
        videoStreamProcessor.start();
        Thread.sleep(10000);
        videoStreamProcessor.stop();
    }
}
