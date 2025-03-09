package com.saigou.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {
    private String secret;
}
