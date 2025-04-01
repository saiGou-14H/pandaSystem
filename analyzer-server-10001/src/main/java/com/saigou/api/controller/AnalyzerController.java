package com.saigou.api.controller;

import cn.hutool.json.JSONUtil;
import com.saigou.api.service.IAnalyzerService;
import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;
import com.saigou.entity.StreamProcessor;
import com.saigou.util.KafkaSendService;
import com.saigou.vo.StreamProcessorVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analyzer")
@RequiredArgsConstructor
public class AnalyzerController {
    private static final Logger log = LoggerFactory.getLogger(AnalyzerController.class);
    private final IAnalyzerService analyzerService;
    private final IMysqlAnalyzerService iMysqlAnalyzerService;
    @GetMapping("/create")
    public StreamProcessorVO create(@RequestParam("id") Long id, @RequestParam("url") String url, @RequestParam("stream") String stream) {
        StreamProcessor streamProcessor = analyzerService.create(id, url,stream);
        StreamProcessorVO streamProcessorVO = new StreamProcessorVO();
        BeanUtils.copyProperties(streamProcessor,streamProcessorVO);
        return streamProcessorVO;
    }

    @GetMapping("/remove/{id}")
    @Transactional
    public void remove(@PathVariable("id") Long id) {
        iMysqlAnalyzerService.removeAnalysisResult(id);
        analyzerService.remove(id);
    }

    @GetMapping("/execute/{id}")
    public void execute(@PathVariable("id") Long id) {
        log.info("布控执行id：{}",id);
        analyzerService.execute(id);
    }

    @GetMapping("/cancel/{id}")
    public void cancel(@PathVariable("id") Long id) {
        log.info("布控取消id：{}",id);
        analyzerService.cancel(id);
    }

    @GetMapping("/list")
    public List<StreamProcessorVO> getAll() {
        List<StreamProcessorVO> collect = analyzerService.getAll().stream().map(streamProcessor -> {
            StreamProcessorVO streamProcessorVO = new StreamProcessorVO();
            BeanUtils.copyProperties(streamProcessor, streamProcessorVO);
            return streamProcessorVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/get/{id}")
    public StreamProcessor getById(@PathVariable("id") Long id) {
        return analyzerService.getById(id);
    }

    @Autowired
    private KafkaSendService kafkaSendService;
    private final IRedisAnalyzerResultService iRedisAnalyzerResultService;
    @GetMapping("/send")
    public void send() {
        log.info("发送kafka消息");
        ControlAnalyzerResult controlAnalyzerResult = iRedisAnalyzerResultService.getAllResult().get(0);
        AnalysisResult analysisResult = controlAnalyzerResult.getResult().get(0);
        kafkaSendService.send(analysisResult);
    }

}
