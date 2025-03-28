package com.saigou.api.service;

import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;

import java.util.List;
import java.util.Map;

public interface IRedisAnalyzerResultService {
    void addAnalysisResult2Hash(Long controlId, Long timestamp, AnalysisResult result);
    AnalysisResult getResultByControlIdAndTimestamp(Long controlId, Long timestamp);
    void removeByControlId(Long controlId);
    void removeByControlIdAndTimestamp(Long controlId, Long timestamp);
    List<AnalysisResult> getResultByControlId(Long controlId);
    List<ControlAnalyzerResult> getAllResult();



}
