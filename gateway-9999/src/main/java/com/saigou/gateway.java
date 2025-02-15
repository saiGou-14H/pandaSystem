package com.saigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class gateway {
    public static void main(String[] args) {
        SpringApplication.run(gateway.class, args);
    }

}