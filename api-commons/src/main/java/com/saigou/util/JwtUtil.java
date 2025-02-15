package com.saigou.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class JwtUtil {
    static final String secret = "70852096";
    public static String getAccessToken(Long id) {
        DateTime date = DateUtil.date();
        //生效时间（JWTPayload.NOT_BEFORE）不能晚于当前时间
        //失效时间（JWTPayload.EXPIRES_AT）不能早于当前时间
        //签发时间（JWTPayload.ISSUED_AT）不能晚于当前时间
        DateTime end_date = DateUtil.offsetHour(date,1);
        String token = cn.hutool.jwt.JWT.create()
                .setNotBefore(date)
                .setIssuedAt(date)
                .setExpiresAt(end_date)
                .setKey(JwtUtil.secret.getBytes())
                .setPayload("id", id)
                .sign();
        return token;
    }
    public static  String getRefreshToken(Long id) {
        DateTime date = DateUtil.date();
        //生效时间（JWTPayload.NOT_BEFORE）不能晚于当前时间
        //失效时间（JWTPayload.EXPIRES_AT）不能早于当前时间
        //签发时间（JWTPayload.ISSUED_AT）不能晚于当前时间
        DateTime end_date = DateUtil.offsetDay(date,1);
        String token = cn.hutool.jwt.JWT.create()
                .setNotBefore(date)
                .setIssuedAt(date)
                .setExpiresAt(end_date)
                .setKey(JwtUtil.secret.getBytes())
                .setPayload("id", id)
                .sign();
        return token;
    }
    public static boolean verifyToken(String token) {
        try {
            JWTValidator.of(token).validateDate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static Map<String, Object> getTokenInfo(String token) {
        return JWTUtil.parseToken(token).getPayloads();
    }
    private static String getHeaderToken(HttpServletRequest request) {
        String token =  request.getHeader("Authorization");
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }


    public static Long getUserId(HttpServletRequest request){
        String token = getHeaderToken(request);
        if (!verifyToken(token)) {
            throw new CustomException(ResponseEnum.TOKEN_EXPIRED);
        }
        Long id = Long.parseLong(getTokenInfo(token).get("id").toString());
        return id;
    }
    public static void main(String[] args) throws InterruptedException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MzUzNzMwODEsImlhdCI6MTczNTM3MzA4MSwiZXhwIjoxNzM1Mzc2NjgxLCJpZCI6MX0.biE6_xBxfOtkwDzsAundESKa5KN6SnaIyzwXXacusZA";
        Thread.sleep(2000);
        System.out.println(verifyToken(token));
        System.out.println(getTokenInfo(token));
    }
}
