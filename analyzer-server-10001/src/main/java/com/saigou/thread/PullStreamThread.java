package com.saigou.thread;

import com.saigou.util.Utils;
import lombok.SneakyThrows;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class PullStreamThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(PullStreamThread.class);
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
        grabber.setOption("hwaccel", "auto"); // 改为自动检测
        grabber.setOption("hwaccel_device", "gpu");  // 指定GPU设备
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start(); // 自动探测分辨率，无需强制设置
    }

    @SneakyThrows
    public void run() {
        Frame frame;
        while (!isInterrupted() && (frame = grabber.grab()) != null) {
            if (frame.image != null) {
                Frame clonedFrame = Utils.createDeepCopy(frame);
                if (frameQueue.remainingCapacity() > 10) {
                    frameQueue.offer(clonedFrame);
                } else {
                    Frame oldFrame = frameQueue.poll();
                    Utils.safeCloseFrame(oldFrame);
                    frameQueue.offer(clonedFrame);
                }
                // 控制日志频率，每30帧打印一次
                if (clonedFrame.timestamp % 30 == 0) {
//                    log.atInfo().log("拉流帧时间戳：" + clonedFrame.timestamp);
                }
            }
        }
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
            log.atError().log("停止抓取器出错：" + e.getMessage());
        }
        // 清空队列并关闭剩余帧
        frameQueue.forEach(Frame::close);
        frameQueue.clear();
        log.atInfo().log("拉流线程终止");
    }
}
