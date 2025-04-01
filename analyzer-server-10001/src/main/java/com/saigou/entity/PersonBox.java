package com.saigou.entity;

import lombok.Data;

@Data
public class PersonBox {
    private String label;
    private Point minPoint;
    private Point maxPoint;
    private float score;
    private long trackId;
    private long attitudeId;
    private String attitudeFeature;
    private String data;
}
