package com.saigou;

import com.google.protobuf.ByteString;
import com.saigou.entity.ImageWrapper;
import com.saigou.grpc.AnalysisResult;
import com.saigou.thread.AnalyzerThread;
import com.saigou.thread.EncodeThread;
import com.saigou.thread.PullStreamThread;
import com.saigou.thread.PushStreamThread;
import org.bytedeco.javacv.Frame;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

class videoStreamProcessor {
    private LinkedBlockingQueue pullframeQueue = new LinkedBlockingQueue<Frame>(70);

    private LinkedBlockingQueue imageQueue = new LinkedBlockingQueue<ImageWrapper>(70);

    private ConcurrentSkipListMap analyzerCache = new ConcurrentSkipListMap<Long, Frame>();

    private LinkedBlockingQueue pushFrameQueue = new LinkedBlockingQueue<ByteString>(70);
    PullStreamThread pullStreamThread;
    EncodeThread encodeThread;
    AnalyzerThread analyzerThread;
    PushStreamThread pushStreamThread;
    videoStreamProcessor(String pullUrl, String pushUrl) {

        this.pullStreamThread = new PullStreamThread(pullUrl,pullframeQueue);
        this.encodeThread = new EncodeThread(pullframeQueue,imageQueue,pushFrameQueue);
        this.analyzerThread = new AnalyzerThread(imageQueue,analyzerCache);
        this.pushStreamThread = new PushStreamThread(pushUrl, pullStreamThread.grabber,pushFrameQueue,analyzerCache);
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