package com.saigou.entity;

import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.properties.AnalyzerProperties;
import com.saigou.properties.PullProperties;
import com.saigou.properties.PushProperties;
import com.saigou.thread.AnalyzerThread;
import com.saigou.thread.EncodeThread;
import com.saigou.thread.PullStreamThread;
import com.saigou.thread.PushStreamThread;
import com.saigou.util.KafkaSendService;
import lombok.Getter;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class StreamProcessor {
    private static final Logger log = LoggerFactory.getLogger(StreamProcessor.class);
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

    KafkaSendService kafkaSendService;
    ThreadPoolExecutor encodingManager;
    ThreadPoolExecutor dencodingManager;
    @Getter
    public Long controlId;
    @Getter
    public String pullUrl;
    @Getter
    public String rtmpPushUrl;
    @Getter
    public String httpPushUrl;
    @Getter
    private volatile boolean running = false; // 新增运行状态标志


    public void initConfig(PullProperties pullProperties, AnalyzerProperties analyzerProperties,
                           PushProperties pushProperties,KafkaSendService kafkaSendService,
                           ThreadPoolExecutor encodingManager,ThreadPoolExecutor dencodingManager) {
        this.pullProperties = pullProperties;
        this.analyzerProperties = analyzerProperties;
        this.pushProperties = pushProperties;
        this.kafkaSendService = kafkaSendService;
        this.encodingManager = encodingManager;
        this.dencodingManager = dencodingManager;
    }
    public void init(Long id,String pullUrl,String rtmpPushUrl ,String httpPushUrl) {
        this.controlId = id;
        this.pullUrl = pullUrl;
        this.rtmpPushUrl = rtmpPushUrl;
        this.httpPushUrl = httpPushUrl;
    }



    public void start() {
        pullStreamThread = new PullStreamThread(pullUrl,pullframeQueue,pullProperties);
        encodeThread = new EncodeThread(pullframeQueue,imageQueue,pushFrameQueue,keyList,encodingManager);
        encodeThread.setFrameRate((int)(this.pullStreamThread.grabber.getFrameRate()*2));// 2秒进行一次算法分析
        analyzerThread = new AnalyzerThread(imageQueue,analyzerCache,analyzerProperties,kafkaSendService,controlId,dencodingManager);
        pushStreamThread = new PushStreamThread(rtmpPushUrl, pullStreamThread.grabber,pushFrameQueue,analyzerCache,keyList,pushProperties);
        pullStreamThread.start();
        encodeThread.start();
        analyzerThread.start();
        pushStreamThread.start();
        running = true;
    }

    public void stop() {
        try{
            pullStreamThread.interrupt();
            encodeThread.interrupt();
            analyzerThread.interrupt();
            pushStreamThread.interrupt();
            // 清空队列并关闭剩余帧
            if(!pullframeQueue.isEmpty()){
                pullframeQueue.forEach(Frame::close);
                pullframeQueue.clear();
            }
            if(!pushFrameQueue.isEmpty()){
                pushFrameQueue.forEach(Frame::close);
                pushFrameQueue.clear();
            }
            if(!imageQueue.isEmpty()){
                imageQueue.clear();
            }
            if(!keyList.isEmpty()){
                keyList.clear();
            }
            if(!analyzerCache.isEmpty()){
                analyzerCache.clear();
            }
        }catch (Exception e){
            log.error("停止线程出错：{}", e);
        }finally {
            running = false;
        }
    }

}