package com.saigou.api.service.Impl;

import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;
import com.saigou.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisAnalyzerResultServiceImpl implements IRedisAnalyzerResultService {

    private static final Logger log = LoggerFactory.getLogger(RedisAnalyzerResultServiceImpl.class);
    private final RedisUtil redisUtil;
    @Override
    public void addAnalysisResult2Hash(Long controlId, Long timestamp, AnalysisResult result) {
        redisUtil.hset("frame:control:"+controlId,String.valueOf(timestamp), result, Duration.ofDays(1).getSeconds());
    }

    @Override
    public AnalysisResult getResultByControlIdAndTimestamp(Long controlId, Long timestamp) {
        return (AnalysisResult) redisUtil.hget("frame:control:"+controlId,String.valueOf(timestamp));
    }

    @Override
    public void removeByControlId(Long controlId) {
        redisUtil.del("frame:control:"+controlId);
    }

    @Override
    public void removeByControlIdAndTimestamp(Long controlId, Long timestamp) {
        redisUtil.hdel("frame:control:"+controlId,String.valueOf(timestamp));
    }

    @Override
    public List<AnalysisResult> getResultByControlId(Long controlId) {
        Map<Object, Object> hmget = redisUtil.hmget("frame:control:" + controlId);
        if(hmget!=null){
            List<AnalysisResult> collect = hmget.values().stream().map(o -> (AnalysisResult) o).sorted().collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public List<ControlAnalyzerResult> getAllResult() {
        long start = System.currentTimeMillis();
        log.info("redis开始查询:getAllResult");
        List<ControlAnalyzerResult> ls = new ArrayList<>();
        redisUtil.getKeys("frame:control:*").forEach(key -> {
            List<AnalysisResult> analysisResults = getResultByControlId(Long.valueOf(key.substring(key.lastIndexOf(":")+1)));
            ls.add(new ControlAnalyzerResult(Long.valueOf(key.substring(key.lastIndexOf(":")+1)),analysisResults));
        });
        log.info("getAllResult:耗时：{}ms",System.currentTimeMillis()-start);
        return ls;
    }
}
