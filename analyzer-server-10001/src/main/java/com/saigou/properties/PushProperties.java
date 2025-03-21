package com.saigou.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "config.push.recorder.video-option")
public class PushProperties {
     private String tune;
     private String preset;
     private String crf;
     private String quality;
     private String rc;
     private String usage;
     private String threads;
     private String rtbufsize;
     private String max_delay;
     private String reconnect;
     private String reconnect_at_eof;
     private String reconnect_streamed;
}
