package com.saigou.thread;

import com.saigou.properties.PullProperties;
import com.saigou.util.Utils;
import lombok.Data;
import lombok.SneakyThrows;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;

public class PullStreamThread extends Thread {
    public FFmpegFrameGrabber grabber;
    public LinkedBlockingQueue<Frame> frameQueue;
    public boolean isAlive = false;
    @SneakyThrows
    public PullStreamThread(String url,
                            LinkedBlockingQueue<Frame> frameQueue,
                            PullProperties pullProperties) {
        this.frameQueue = frameQueue;
        this.grabber = new FFmpegFrameGrabber(url);
        grabber.setImageHeight(pullProperties.getMaxImageHeight());
        grabber.setImageWidth(pullProperties.getMaxImageWidth());
        grabber.setOption("hwaccel", pullProperties.getHwaccel()); // 改为自动检测
        grabber.setOption("hwaccel_device", pullProperties.getHwaccel_device());  // 指定GPU设备
        grabber.setOption("rtsp_transport", pullProperties.getRtsp_transport());
        grabber.start();
    }
    @SneakyThrows
    public void run() {
        isAlive = true;
        Frame frame;
        while (!isInterrupted() && (frame = grabber.grab()) != null) {
            if (frame.image != null) {
                frame.timestamp = System.currentTimeMillis();
                Frame clonedFrame = Utils.createDeepCopy(frame);
                if (frameQueue.remainingCapacity() > 10) {
                    frameQueue.offer(clonedFrame);
                } else {
                    Frame oldFrame = frameQueue.poll();
                    Utils.safeCloseFrame(oldFrame);
                    frameQueue.offer(clonedFrame);
                }
            }
        }
        isAlive = false;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.release();
                grabber.close();
            }
        } catch (FrameGrabber.Exception e) {
            System.out.println("停止抓取器出错：" + e.getMessage());
        }
        // 清空队列并关闭剩余帧
        frameQueue.forEach(Frame::close);
        frameQueue.clear();
        System.out.println("拉流线程终止");
    }
}
