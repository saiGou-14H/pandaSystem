package com.saigou;

import com.saigou.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(defaultConfiguration = DefaultFeignConfig.class)
public class roomConsumer {
    public static void main(String[] args) {
        SpringApplication.run(roomConsumer.class, args);
    }
}