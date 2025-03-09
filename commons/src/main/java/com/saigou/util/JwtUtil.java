package com.saigou.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.saigou.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Map;
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    public String getAccessToken(Long id) {
        DateTime date = DateUtil.date();
        //生效时间（JWTPayload.NOT_BEFORE）不能晚于当前时间
        //失效时间（JWTPayload.EXPIRES_AT）不能早于当前时间
        //签发时间（JWTPayload.ISSUED_AT）不能晚于当前时间
        DateTime end_date = DateUtil.offsetHour(date,1);
        String token = JWT.create()
                .setNotBefore(date)
                .setIssuedAt(date)
                .setExpiresAt(end_date)
                .setKey(jwtProperties.getSecret().getBytes())
                .setPayload("id", id)
                .sign();
        return token;
    }
    public  String getRefreshToken(Long id) {
        DateTime date = DateUtil.date();
        //生效时间（JWTPayload.NOT_BEFORE）不能晚于当前时间
        //失效时间（JWTPayload.EXPIRES_AT）不能早于当前时间
        //签发时间（JWTPayload.ISSUED_AT）不能晚于当前时间
        DateTime end_date = DateUtil.offsetDay(date,1);
        String token = JWT.create()
                .setNotBefore(date)
                .setIssuedAt(date)
                .setExpiresAt(end_date)
                .setKey(jwtProperties.getSecret().getBytes())
                .setPayload("id", id)
                .sign();
        return token;
    }
    public boolean verifyToken(String token) {
        try {
            JWTValidator.of(token).validateDate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public Map<String, Object> getTokenInfo(String token) {
        return JWTUtil.parseToken(token).getPayloads();
    }
    public String getHeaderToken(ServerHttpRequest request) {
        String token =  request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }


    public Long getUserId(String token){
        Long id = Long.parseLong(getTokenInfo(token).get("id").toString());
        return id;
    }
    public static void main(String[] args) throws InterruptedException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MzUzNzMwODEsImlhdCI6MTczNTM3MzA4MSwiZXhwIjoxNzM1Mzc2NjgxLCJpZCI6MX0.biE6_xBxfOtkwDzsAundESKa5KN6SnaIyzwXXacusZA";
    }
}
