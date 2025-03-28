package com.saigou;

import com.saigou.properties.AnalyzerProperties;
import com.saigou.properties.PullProperties;
import com.saigou.properties.PushProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({PullProperties.class, AnalyzerProperties.class, PushProperties.class})
public class analyzerServer {
    public static void main(String[] args){
        SpringApplication.run(analyzerServer.class, args);
    }
}