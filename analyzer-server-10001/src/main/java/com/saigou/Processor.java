package com.saigou;

import org.bytedeco.javacv.*;
public class Processor {
    static {
        // 启用FFmpeg详细日志
        FFmpegLogCallback.set();
    }
    public static void main(String[] args) throws InterruptedException {
        String url1 = "rtsp://127.0.0.1:7554/live/60";
        String url2 = "rtmp://127.0.0.1:7935/live/test3";
        videoStreamProcessor videoStreamProcessor = new videoStreamProcessor(url1, url2);
        videoStreamProcessor.start();
        Thread.sleep(100000000);
        videoStreamProcessor.stop();
    }
}
