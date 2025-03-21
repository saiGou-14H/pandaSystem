package com.saigou.vo;

import lombok.Data;

@Data
public class StreamProcessorVO {
    private Long controlId;
    private String pullUrl;
    private String rtmpPushUrl;
    private String httpPushUrl;
    private boolean isAlive = false;

}