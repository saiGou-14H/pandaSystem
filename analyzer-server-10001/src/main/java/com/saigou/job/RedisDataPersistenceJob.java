package com.saigou.job;

import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RedisDataPersistenceJob {

    @Autowired
    private IRedisAnalyzerResultService iRedisAnalyzerResultService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void doWork() {
//        System.out.println("---------------vo对象持久化-begin----------------");
////        从redis中获取所有vo对象             keys strategy_statis_vo:*
//        List<ControlAnalyzerResult> vos = iRedisAnalyzerResultService.getAllResult();
////        遍历vo对象,将数据更新到对应攻略表中
////        for (List<AnalysisResult> vo : vos) {
////            System.out.println("vo:" + vo);
////        }
//        System.out.println("---------------vo对象持久化-end----------------");
    }
}
