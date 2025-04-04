package com.saigou.api.controller;

import com.saigou.api.service.IControlTimestampService;
import com.saigou.entity.ControlTimestamp;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyzer/controltimestamp")
public class ControlTimestampController {
    private final IControlTimestampService controlTimestampService;

    @GetMapping("/list/{controlId}")
    @Operation(summary = "根据controlId获取分析结果")
    public ResponseVO list(@PathVariable("controlId") Long controlId) {
        return ResponseVO.success(controlTimestampService.getByControlId(controlId));
    }

    @GetMapping("/{controlId}/{timestamp}")
    @Operation(summary = "根据controlId和timestamp获取分析结果")
    public ResponseVO listByControlIdAndTimestamp(@PathVariable("controlId") Long controlId, @PathVariable("timestamp") Long timestamp) {
        return ResponseVO.success(controlTimestampService.getByControlIdAndTimestamp(controlId, timestamp));
    }

    @GetMapping("/list_start_end/{controlId}/{start_timestamp}/{end_timestamp}")
    @Operation(summary = "根据controlId和timestamp范围获取分析结果")
    public ResponseVO listByControlIdAndTimestampRange(@PathVariable("controlId")Long controlId, @PathVariable("start_timestamp")Long start_timestamp, @PathVariable("end_timestamp")Long end_timestamp) {
        return ResponseVO.success(controlTimestampService.getByControlIdAndTimestampBetween(controlId,start_timestamp,end_timestamp));
    }

    @GetMapping("/listByMinutesAgo/{controlId}/{minutes}")
    @Operation(summary = "根据controlId和timestamp范围获取分析结果")
    public ResponseVO listByControlIdAndTimestampByMinutesAgo(@PathVariable("controlId")Long controlId, @PathVariable("minutes")Integer minutes) {
        return ResponseVO.success(controlTimestampService.listByControlIdAndTimestampByMinutesAgo(controlId,minutes));
    }
}
