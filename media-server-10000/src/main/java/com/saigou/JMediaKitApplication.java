package com.saigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 流媒体服务器
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class JMediaKitApplication {
    public static void main(String[] args) {
        SpringApplication.run(JMediaKitApplication.class, args);
    }
}
