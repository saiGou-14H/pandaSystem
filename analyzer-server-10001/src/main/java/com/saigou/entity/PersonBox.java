package com.saigou.entity;

import lombok.Data;

@Data
public class PersonBox {
    private String label;
    private Point minPoint;
    private Point maxPoint;
    private float score;
    private long track_id;
    private long attitude_id;
    private String expression_feature;
    private String data;
}
