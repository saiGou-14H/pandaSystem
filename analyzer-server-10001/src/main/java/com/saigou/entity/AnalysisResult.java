package com.saigou.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult implements Serializable,Comparable<AnalysisResult> {
    private long timestamp;
    private List<FaceBox> faceBoxes;
    private List<PersonBox> personBoxes;
    private ControlTimestamp controlTimestamp;
    private String data;
    private byte[] imageData;

    @Override
    public int compareTo(@NotNull AnalysisResult o) {
        return o.getTimestamp()<this.getTimestamp()?1:-1;
    }
}
