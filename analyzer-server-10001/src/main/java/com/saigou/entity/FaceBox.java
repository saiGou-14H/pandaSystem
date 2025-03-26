package com.saigou.entity;

import lombok.Data;

@Data
public class FaceBox {
    private String label;
    private Point minPoint;
    private Point maxPoint;
    private float score;
    private long track_id;
    private long face_id;
    private long expression_id;
    private String expression_feature;
    private String data;
}
