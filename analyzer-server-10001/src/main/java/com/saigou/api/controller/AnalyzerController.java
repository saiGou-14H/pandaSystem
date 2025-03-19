package com.saigou.api.controller;

import com.saigou.context.AnalyzerContext;
import com.saigou.util.ResponseVO;
import com.saigou.util.StreamProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyzer")
@RequiredArgsConstructor
public class AnalyzerController {
    private final AnalyzerContext analyzerContext;

    @PostMapping("/add/{id}/{url}")
    @Autowired(required = false)
    public ResponseVO add(StreamProcessor streamProcessor, @PathVariable Long id, String url) {
        streamProcessor.init(url,url.replace("live","analyzer"));
        streamProcessor.start();
        analyzerContext.addStreamProcessor(id,streamProcessor);
        return ResponseVO.success();
    }
}
