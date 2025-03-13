package com.saigou.util;

import com.saigou.entity.FrameWrapper;
import com.saigou.entity.ImageWrapper;
import com.saigou.properties.AnalyzerProperties;
import com.saigou.properties.PullProperties;
import com.saigou.properties.PushProperties;
import com.saigou.thread.AnalyzerThread;
import com.saigou.thread.EncodeThread;
import com.saigou.thread.PullStreamThread;
import com.saigou.thread.PushStreamThread;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.Frame;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
@Component
@Scope("prototype")
@RequiredArgsConstructor
@EnableConfigurationProperties({PullProperties.class, AnalyzerProperties.class, PushProperties.class})
public class StreamProcessor {
    private LinkedBlockingQueue<Frame> pullframeQueue = new LinkedBlockingQueue<>(70);
    private LinkedBlockingQueue<ImageWrapper> imageQueue = new LinkedBlockingQueue<>(70);
    private LinkedBlockingQueue<Frame> pushFrameQueue = new LinkedBlockingQueue<>(70);
    private CopyOnWriteArrayList<Long> keyList= new CopyOnWriteArrayList<Long>();
    private ConcurrentSkipListMap<Long, FrameWrapper> analyzerCache = new ConcurrentSkipListMap<>();


    private final PullProperties pullProperties;
    private final AnalyzerProperties analyzerProperties;
    private final PushProperties pushProperties;


    PullStreamThread pullStreamThread;
    EncodeThread encodeThread;
    AnalyzerThread analyzerThread;
    PushStreamThread pushStreamThread;
    public void init(String pullUrl, String pushUrl) {
        this.pullStreamThread = new PullStreamThread(pullUrl,pullframeQueue,pullProperties);
        this.encodeThread = new EncodeThread(pullframeQueue,imageQueue,pushFrameQueue,keyList);
        this.analyzerThread = new AnalyzerThread(imageQueue,analyzerCache,analyzerProperties);
        this.pushStreamThread = new PushStreamThread(pushUrl, pullStreamThread.grabber,pushFrameQueue,analyzerCache,keyList,pushProperties);
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