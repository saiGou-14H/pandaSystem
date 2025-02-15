package com.saigou;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan(basePackages = "com.saigou.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class Main9000 {
    public static void main(String[] args) {
        SpringApplication.run(Main9000.class, args);
    }

}
