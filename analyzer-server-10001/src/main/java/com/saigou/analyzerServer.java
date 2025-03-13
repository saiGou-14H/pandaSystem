package com.saigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class analyzerServer {
    public static void main(String[] args){
        SpringApplication.run(analyzerServer.class, args);
    }
}