package com.saigou.api.controller;

import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mysql")
@RequiredArgsConstructor
@Tag(name = "test_mysql")
public class MysqlAnalyzerController {
    private final IMysqlAnalyzerService mysqlAnalyzerService;

    @GetMapping("/list")
    @Operation(summary = "获取全部")
    public List<ControlAnalyzerResult> list() {
        return mysqlAnalyzerService.getAnalysisResult();
    }

    @GetMapping("/listByControlId/{controlId}")
    @Operation(summary = "根据布控id查找")
    public ControlAnalyzerResult listByControlId(@PathVariable("controlId") Long controlId) {
        return mysqlAnalyzerService.getAnalysisResult(controlId);
    }

    @GetMapping("/listByControlIdAndTimestamp/{controlId}/{timestamp}")
    @Operation(summary = "根据布控id和timestamp查找")
    public AnalysisResult listByControlIdAndTimestamp(@PathVariable("controlId") Long controlId, @PathVariable("timestamp")Long timestamp) {
        return mysqlAnalyzerService.getAnalysisResult(controlId, timestamp);
    }

    @GetMapping("/listByControlIdAndTimestampRange/{controlId}/{start_timestamp}/{end_timestamp}")
    @Operation(summary = "根据布控id和timestamp范围查找")
    public ControlAnalyzerResult listByControlIdAndTimestampRange(@PathVariable("controlId")Long controlId, @PathVariable("start_timestamp")Long start_timestamp, @PathVariable("end_timestamp")Long end_timestamp) {
        return mysqlAnalyzerService.getAnalysisResult(controlId, start_timestamp, end_timestamp);
    }

    @GetMapping("/removeByControlId/{controlId}")
    @Operation(summary = "根据布控id删除")
    public void removeByControlId(@PathVariable("controlId")Long controlId) {
        mysqlAnalyzerService.removeAnalysisResult(controlId);
    }

    @GetMapping("/removeByControlIdAndTimestamp/{controlId}/{timestamp}")
    @Operation(summary = "根据布控id和timestamp删除")
    public void removeByControlIdAndTimestamp(@PathVariable("controlId")Long controlId, @PathVariable("timestamp") Long timestamp) {
        mysqlAnalyzerService.removeAnalysisResult(controlId, timestamp);
    }

    @PostMapping("/addAnalysisResult/{controlId}")
    @Operation(summary = "按帧添加分析结果")
    public void addAnalysisResult(@PathVariable("controlId")Long controlId,@RequestBody AnalysisResult result) {
        mysqlAnalyzerService.addAnalysisResult(controlId, result);
    }


}
