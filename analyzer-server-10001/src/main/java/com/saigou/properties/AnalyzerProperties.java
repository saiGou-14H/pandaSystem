package com.saigou.properties;

import com.saigou.entity.Address;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "config.analyzer")
public class AnalyzerProperties {
    private int maxInboundMessageSize;
    private List<Address> server;
}
