package com.saigou.context;

import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.properties.AnalyzerProperties;
import com.saigou.properties.PullProperties;
import com.saigou.properties.PushProperties;
import com.saigou.entity.StreamProcessor;
import com.saigou.util.JwtUtil;
import com.saigou.util.KafkaSendService;
import com.saigou.util.RedisUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Component
@RequiredArgsConstructor
public class AnalyzerContext {
    private final String ANALYZER_CONTEXT_KEY = "analyzerContext";
    private final String APP = "analyzer";
    @Value("${media.host}")
    private String DEFAULT_HOST;
    @Value("${media.rtmp_port}")
    private String DEFAULT_RTMP_PORT;
    @Value("${media.rtsp_port}")
    private String DEFAULT_RTSP_PORT;
    @Value("${media.http_port}")
    private String DEFAULT_HTTP_PORT;
    @Value("${media.rtc_port}")
    private String DEFAULT_RTC_PORT;


    private final PullProperties pullProperties;
    private final AnalyzerProperties analyzerProperties;
    private final PushProperties pushProperties;
    private final Map<Long, StreamProcessor> streamProcessorMap = new HashMap<>();
    private final IRedisAnalyzerResultService iRedisAnalyzerResultService;
    private final KafkaSendService kafkaSendService;
    private final ThreadPoolExecutor encodingManager = new ThreadPoolExecutor(17, 100,
            30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardOldestPolicy());
    private final ThreadPoolExecutor dencodingManager = new ThreadPoolExecutor(17, 100,
            30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardOldestPolicy());


    public List<StreamProcessor> getAllStreamProcessor() {
        return streamProcessorMap.values().stream().toList();
    }

    public StreamProcessor getStreamProcessor(Long id) {
        return streamProcessorMap.get(id);
    }

    public StreamProcessor addStreamProcessor(Long id, String url,String stream) {
        if (streamProcessorMap.containsKey(id)) {
            return streamProcessorMap.get(id);
        }
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.initConfig(pullProperties,analyzerProperties,pushProperties,iRedisAnalyzerResultService,kafkaSendService,encodingManager,dencodingManager);
        String rtmpPushUrl = "rtmp://"+DEFAULT_HOST+":"+DEFAULT_RTMP_PORT+"/"+APP+"/"+stream;
        String httpPushUrl = "http://"+DEFAULT_HOST+":"+DEFAULT_HTTP_PORT+"/"+APP+"/"+stream+".live.flv";
        streamProcessor.init(id,url,rtmpPushUrl,httpPushUrl);
        streamProcessorMap.put(id, streamProcessor);
       return streamProcessor;
    }

    public void executeStreamProcessor(Long id){
        StreamProcessor oldstreamProcessor = streamProcessorMap.get(id);
        if(oldstreamProcessor==null || oldstreamProcessor.isAlive()){
            return;
        }
        StreamProcessor newstreamProcessor = new StreamProcessor();
        newstreamProcessor.initConfig(pullProperties,analyzerProperties,pushProperties,iRedisAnalyzerResultService,kafkaSendService,encodingManager,dencodingManager);
        newstreamProcessor.init(id,oldstreamProcessor.getPullUrl(),oldstreamProcessor.getRtmpPushUrl(),oldstreamProcessor.httpPushUrl);
        removeStreamProcessor(id);
        newstreamProcessor.start();
        streamProcessorMap.put(id, newstreamProcessor);
    }

    public void cancelStreamProcessor(Long id){
        StreamProcessor streamProcessor = streamProcessorMap.get(id);
        if(streamProcessor==null || !streamProcessor.isAlive()){
            return;
        }
        streamProcessor.stop();
    }

    public void removeStreamProcessor(Long id){
        StreamProcessor streamProcessor = streamProcessorMap.get(id);
        if(streamProcessor==null){
            return;
        }
        if(streamProcessor.isAlive()){
            streamProcessor.stop();
        }
        streamProcessorMap.remove(id);
    }

    public void clear() {
        stopAll();
        streamProcessorMap.clear();
    }
    public void stopAll() {
        streamProcessorMap.forEach((k, v) -> v.stop());
    }
    public void startAll() {
        streamProcessorMap.forEach((k, v) -> v.start());
    }
}
