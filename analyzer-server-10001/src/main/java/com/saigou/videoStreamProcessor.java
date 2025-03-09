package com.saigou;

import com.saigou.entity.FrameWrapper;
import com.saigou.entity.ImageWrapper;
import com.saigou.thread.AnalyzerThread;
import com.saigou.thread.EncodeThread;
import com.saigou.thread.PullStreamThread;
import com.saigou.thread.PushStreamThread;
import org.bytedeco.javacv.Frame;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

class videoStreamProcessor {
    private LinkedBlockingQueue<Frame> pullframeQueue = new LinkedBlockingQueue<>(90);

    private LinkedBlockingQueue<ImageWrapper> imageQueue = new LinkedBlockingQueue<>(90);

    private CopyOnWriteArrayList<Long> keyList= new CopyOnWriteArrayList<Long>();

    private ConcurrentSkipListMap<Long, FrameWrapper> analyzerCache = new ConcurrentSkipListMap<>();

    private LinkedBlockingQueue<Frame> pushFrameQueue = new LinkedBlockingQueue<>(90);

    PullStreamThread pullStreamThread;
    EncodeThread encodeThread;
    AnalyzerThread analyzerThread;
    PushStreamThread pushStreamThread;
    videoStreamProcessor(String pullUrl, String pushUrl) {
        this.pullStreamThread = new PullStreamThread(pullUrl,pullframeQueue);
        this.encodeThread = new EncodeThread(pullframeQueue,imageQueue,pushFrameQueue,keyList);
        this.analyzerThread = new AnalyzerThread(imageQueue,analyzerCache);
        this.pushStreamThread = new PushStreamThread(pushUrl, pullStreamThread.grabber,pushFrameQueue,analyzerCache,keyList);
    }
    public void start() {
        pullStreamThread.start();
        encodeThread.start();
        analyzerThread.start();
        pushStreamThread.start();
    }

    public void stop() {
        pullStreamThread.interrupt();
        encodeThread.interrupt();
        analyzerThread.interrupt();
        pushStreamThread.interrupt();
    }

}