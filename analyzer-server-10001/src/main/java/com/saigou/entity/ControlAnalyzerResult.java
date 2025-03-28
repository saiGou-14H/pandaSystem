package com.saigou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlAnalyzerResult {
    private Long controlId;
    private List<AnalysisResult> result;
}
