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
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.Frame;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
public class StreamProcessor {
    private LinkedBlockingQueue<Frame> pullframeQueue = new LinkedBlockingQueue<>(70);
    private LinkedBlockingQueue<ImageWrapper> imageQueue = new LinkedBlockingQueue<>(70);
    private LinkedBlockingQueue<Frame> pushFrameQueue = new LinkedBlockingQueue<>(70);
    private CopyOnWriteArrayList<Long> keyList= new CopyOnWriteArrayList<Long>();
    private ConcurrentSkipListMap<Long, FrameWrapper> analyzerCache = new ConcurrentSkipListMap<>();
    private PullProperties pullProperties;
    private AnalyzerProperties analyzerProperties;
    private PushProperties pushProperties;
    PullStreamThread pullStreamThread;
    EncodeThread encodeThread;
    AnalyzerThread analyzerThread;
    PushStreamThread pushStreamThread;
    @Getter
    private Long controlId;
    @Getter
    private String pullUrl;
    @Getter
    private String rtmpPushUrl;
    @Getter
    private String httpPushUrl;
    @Getter
    private boolean isAlive = false;


    public void initConfig(PullProperties pullProperties, AnalyzerProperties analyzerProperties, PushProperties pushProperties) {
        this.pullProperties = pullProperties;
        this.analyzerProperties = analyzerProperties;
        this.pushProperties = pushProperties;
    }
    public void init(Long id,String pullUrl,String rtmpPushUrl ,String httpPushUrl) {
        this.controlId = id;
        this.pullUrl = pullUrl;
        this.rtmpPushUrl = rtmpPushUrl;
        this.httpPushUrl = httpPushUrl;

    }

    public void start() {
        this.pullStreamThread = new PullStreamThread(pullUrl,pullframeQueue,pullProperties);
        this.encodeThread = new EncodeThread(pullframeQueue,imageQueue,pushFrameQueue,keyList);
        this.analyzerThread = new AnalyzerThread(imageQueue,analyzerCache,analyzerProperties);
        this.pushStreamThread = new PushStreamThread(rtmpPushUrl, pullStreamThread.grabber,pushFrameQueue,analyzerCache,keyList,pushProperties);
        pullStreamThread.start();
        encodeThread.start();
        analyzerThread.start();
        pushStreamThread.start();
        isAlive = true;
    }

    public void stop() {
        pushStreamThread.interrupt();
        analyzerThread.interrupt();
        encodeThread.interrupt();
        pullStreamThread.interrupt();
        isAlive = false;
    }

}