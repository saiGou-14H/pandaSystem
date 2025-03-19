package com.saigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CourseProvider {
    public static void main(String[] args) {
        SpringApplication.run(CourseProvider.class, args);
    }
}