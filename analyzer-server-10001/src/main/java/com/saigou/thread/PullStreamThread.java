package com.saigou.thread;

import lombok.SneakyThrows;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import java.util.concurrent.LinkedBlockingQueue;

public class PullStreamThread extends Thread{
    public FFmpegFrameGrabber grabber;
    public LinkedBlockingQueue<Frame> frameQueue;


    public final int maxImageWidth = 1920;
    public final int maxImageHeight = 1080;
    @SneakyThrows
    public PullStreamThread(String url, LinkedBlockingQueue<Frame> frameQueue) {
        this.frameQueue = frameQueue;
        this.grabber = new FFmpegFrameGrabber(url);
        //关键配置：启用AMF硬件解码
        grabber.setImageHeight(maxImageHeight);
        grabber.setImageWidth(maxImageWidth);
        grabber.setOption("hwaccel", "amf");         // 指定使用AMF加速
        grabber.setOption("hwaccel_device", "gpu");  // 指定GPU设备
//        grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();
    }
    @SneakyThrows
    public void run() {
        Frame frame;
        while (!isInterrupted() && (frame = grabber.grab()) != null) {
            if (frame!=null && frame.image != null){
                if (frameQueue.remainingCapacity() > 10) { // 保持缓冲余量
                    frameQueue.offer(frame.clone());
                } else {
                    // 丢弃旧帧保持实时性
                    Frame oldFrame = frameQueue.poll();
                    oldFrame.close();
                    frameQueue.offer(frame.clone());
                }
            }
        }
    }
    @Override
    public void interrupt(){
        super.interrupt();
        if (grabber != null) {
            try {
                grabber.stop();    // 停止抓取
                grabber.release(); // 释放抓取资源
                grabber.close();   // 关闭连接
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException("停止或释放抓取资源时出错", e);
            }
        }
        System.out.println("拉流结束");
    }
}