package com.saigou.thread;

import com.saigou.properties.PullProperties;
import com.saigou.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;

@EqualsAndHashCode(callSuper = true)
@Data
public class PullStreamThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(PullStreamThread.class);
    public FFmpegFrameGrabber grabber;
    public LinkedBlockingQueue<Frame> frameQueue;

    @SneakyThrows
    public PullStreamThread(String url,LinkedBlockingQueue<Frame> frameQueue,PullProperties pullProperties){
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
        Frame frame;
        while (!isInterrupted() && (frame = grabber.grab()) != null) {
            try {
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
            } catch (Exception e) {
            log.error("拉流线程异常",e);
            }
        }
    }

    @Override
    public void interrupt() {
        if(!isInterrupted()){
            super.interrupt();
        }
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.release();
                grabber.close();
            }
        } catch (FrameGrabber.Exception e) {
            log.error("停止抓取器出错：{}", e);
        }

        log.info("拉流结束，时间：{}", LocalDateTime.now());
    }
}
