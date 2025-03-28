package com.saigou.entity;

import lombok.Data;

@Data
public class FaceBox {
    private String label;
    private Point minPoint;
    private Point maxPoint;
    private float score;
    private long trackId;
    private long faceId;
    private long expressionId;
    private String expressionFeature;
    private String data;
}
