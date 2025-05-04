package com.saigou.thread;

import com.saigou.properties.PullProperties;
import com.saigou.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private volatile boolean running = true; // 新增运行状态标志
    public FFmpegFrameGrabber grabber;
    public LinkedBlockingQueue<Frame> pullframeQueue;
    private final Object grabberLock = new Object(); // 新增锁对象

    public PullStreamThread(String url, LinkedBlockingQueue<Frame> pullframeQueue, PullProperties pullProperties) {
        this.pullframeQueue = pullframeQueue;
        this.grabber = new FFmpegFrameGrabber(url);
        try {
            grabber.setImageHeight(pullProperties.getMaxImageHeight());
            grabber.setImageWidth(pullProperties.getMaxImageWidth());
            grabber.setOption("hwaccel", pullProperties.getHwaccel());
            grabber.setOption("hwaccel_device", pullProperties.getHwaccel_device());
            grabber.setOption("rtsp_transport", pullProperties.getRtsp_transport());
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            log.error("初始化 FFmpegFrameGrabber 失败", e);
            throw new RuntimeException("拉流初始化失败", e); // 抛出异常通知上层
        }
    }

    @Override
    public void run() {
        Frame frame;
        try {
            while (running && !isInterrupted()) {
                synchronized (grabberLock) { // 保护 grab 操作
                    if (grabber == null) break; // 已释放则退出
                    frame = grabber.grab();
                    if (frame == null) {
                        log.warn("拉流结束，获取到空帧");
                        break;
                    }
                    processFrame(frame);
                }
            }
        } catch (FrameGrabber.Exception e) {
            if (running) {
                log.error("拉流线程异常", e);
            }
        } finally {
            safeReleaseGrabber();
        }
    }
    private void processFrame(Frame frame) {
        if (frame.image == null) return;
        try {
            frame.timestamp = System.currentTimeMillis();
            Frame clonedFrame = Utils.createDeepCopy(frame);
            // 简化队列操作，依赖阻塞队列的线程安全性
            if (!pullframeQueue.offer(clonedFrame)) {
                Frame oldFrame = pullframeQueue.poll(); // 队列满时移除最旧元素
                Utils.safeCloseFrame(oldFrame);
                pullframeQueue.offer(clonedFrame);
            }
        } catch (Exception e) {
            log.error("帧处理异常", e);
            Utils.safeCloseFrame(frame); // 确保原始帧关闭
        }
    }

    private void safeReleaseGrabber() {
        synchronized (grabberLock) { // 确保原子性操作
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                    grabber.close();
                }
            } catch (FrameGrabber.Exception e) {
                log.error("释放抓取器资源失败", e);
            } finally {
                grabber = null;
            }
        }
    }
    @Override
    public void interrupt() {
        running = false; // 先标记停止，避免后续处理
        super.interrupt(); // 触发中断状态
        safeReleaseGrabber(); // 释放资源
        log.info("拉流结束，时间：{}", LocalDateTime.now());
    }
}