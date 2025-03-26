package com.saigou.api.controller;

import com.saigou.api.service.IAnalyzerService;
import com.saigou.entity.StreamProcessor;
import com.saigou.vo.StreamProcessorVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analyzer")
@RequiredArgsConstructor
public class AnalyzerController {
    private static final Logger log = LoggerFactory.getLogger(AnalyzerController.class);
    private final IAnalyzerService analyzerService;
    @GetMapping("/create")
    public StreamProcessorVO create(@RequestParam("id") Long id, @RequestParam("url") String url, @RequestParam("stream") String stream) {
        StreamProcessor streamProcessor = analyzerService.create(id, url,stream);
        StreamProcessorVO streamProcessorVO = new StreamProcessorVO();
        BeanUtils.copyProperties(streamProcessor,streamProcessorVO);
        return streamProcessorVO;
    }

    @GetMapping("/remove/{id}")
    public void remove(@PathVariable("id") Long id) {
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

}
