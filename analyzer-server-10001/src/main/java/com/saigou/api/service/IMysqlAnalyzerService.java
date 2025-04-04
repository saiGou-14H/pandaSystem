package com.saigou.api.service;

import com.saigou.entity.AnalysisResult;
import com.saigou.entity.ControlAnalyzerResult;

import java.util.List;

public interface IMysqlAnalyzerService {
    void addAnalysisResult(Long controlId,AnalysisResult result);
    void removeAnalysisResult(Long controlId, Long timestamp);
    void removeAnalysisResult(Long controlId);
    AnalysisResult getAnalysisResult(Long controlId, Long timestamp);
    ControlAnalyzerResult getAnalysisResult(Long controlId, Long start_timestamp, Long end_timestamp);
    ControlAnalyzerResult getAnalysisResult(Long controlId);
    List<ControlAnalyzerResult> getAnalysisResult();
    List<ControlAnalyzerResult> getAnalysisResult(Long controlId, Integer minutes);
}
