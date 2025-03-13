package com.saigou.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "config.pull")
public class PullProperties {
     private int maxImageWidth;
     private int maxImageHeight;
     private String rtsp_transport;
     private String hwaccel;
     private String hwaccel_device;
}
