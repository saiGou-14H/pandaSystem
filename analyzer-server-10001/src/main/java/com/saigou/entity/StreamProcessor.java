package com.saigou.entity;

import com.saigou.properties.AnalyzerProperties;
import com.saigou.properties.PullProperties;
import com.saigou.properties.PushProperties;
import com.saigou.thread.AnalyzerThread;
import com.saigou.thread.EncodeThread;
import com.saigou.thread.PullStreamThread;
import com.saigou.thread.PushStreamThread;
import com.saigou.util.RedisUtil;
import lombok.Getter;
import org.bytedeco.javacv.Frame;

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
    RedisUtil redisUtil;
    @Getter
    public Long controlId;
    @Getter
    public String pullUrl;
    @Getter
    public String rtmpPushUrl;
    @Getter
    public String httpPushUrl;
    @Getter
    public boolean isAlive = false;


    public void initConfig(PullProperties pullProperties, AnalyzerProperties analyzerProperties, PushProperties pushProperties, RedisUtil redisUtil) {
        this.pullProperties = pullProperties;
        this.analyzerProperties = analyzerProperties;
        this.pushProperties = pushProperties;
        this.redisUtil = redisUtil;
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
        this.analyzerThread = new AnalyzerThread(imageQueue,analyzerCache,analyzerProperties,redisUtil,controlId);
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