package com.saigou.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnalysisResult implements Serializable,Comparable<AnalysisResult> {
    private long timestamp;
    private List<FaceBox> faceBoxes;
    private List<PersonBox> personBoxes;
    private String data;
    private byte[] imageData;

    @Override
    public int compareTo(@NotNull AnalysisResult o) {
        return o.getTimestamp()<this.getTimestamp()?1:-1;
    }
}
