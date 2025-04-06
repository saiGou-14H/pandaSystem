package com.saigou.api.controller;

import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.saigou.api.service.IRedisAnalyzerResultService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController("/analyzer/redis")
@Tag(name = "/analyzer/redis")
public class RedisAnalyzerResultController {
    private final IRedisAnalyzerResultService iRedisAnalyzerResultService;

    @GetMapping("/list")
    @Operation(summary = "获取所有结果")
    public List<ControlAnalyzerResult> getAllResult() {
        return iRedisAnalyzerResultService.getAllResult();
    }

    @GetMapping("/get/{controlId}")
    @Operation(summary = "获取结果")
    public List<AnalysisResult> getResultByControlId(@PathVariable("controlId") Long controlId) {
        return iRedisAnalyzerResultService.getResultByControlId(controlId);
    }

    @GetMapping("/get/{controlId}/{timestamp}")
    @Operation(summary = "获取结果")
    public AnalysisResult getResultByControlIdAndTimestamp(@PathVariable("controlId") Long controlId, @PathVariable("timestamp") Long timestamp) {
        return iRedisAnalyzerResultService.getResultByControlIdAndTimestamp(controlId, timestamp);
    }

}
