package com.saigou.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnalysisResult implements Serializable,Comparable<AnalysisResult> {
    private long timestamp;
    private List<FaceBox> face_boxes;
    private List<PersonBox> person_boxes;
    private String data;
    private byte[] image_data;

    @Override
    public int compareTo(@NotNull AnalysisResult o) {
        return o.getTimestamp()>this.getTimestamp()?1:-1;
    }
}
