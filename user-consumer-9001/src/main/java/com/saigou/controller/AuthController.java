package com.saigou.controller;

import com.saigou.api.UserApi;
import com.saigou.entity.User;
import com.saigou.util.*;
import com.saigou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
@Tag(name="登录微服务")
@RequiredArgsConstructor
public class AuthController {
    private final UserApi userApi;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    @PostMapping("login")
    @Operation(summary = "登录",description = "根据账号密码登录")
    public ResponseVO login(@RequestBody User user){
        User auth = userApi.get(null,user.getAccount());
        if (auth==null)
            return ResponseVO.error(ResponseEnum.ACCOUNT_NOT_EXIST);
        if (!auth.getPassword().equals(user.getPassword()))
            return ResponseVO.error(ResponseEnum.INCORRECT_CREDENTIALS);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(auth,userVo);
        userVo.setAccessToken(jwtUtil.getAccessToken(auth.getId()));
        userVo.setRefreshToken(jwtUtil.getRefreshToken(auth.getId()));
        //Redis保存token
        redisTemplate.opsForValue().set("user:"+auth.getId(),userVo.getRefreshToken(),60*60*1);
        return ResponseVO.success(userVo);
    }
    @GetMapping("refresh")
    @Operation(summary = "刷新token",description = "刷新token")
    public ResponseVO refresh(){
        Long userId = AuthContext.getId();
        UserVo userVo = new UserVo();
        userVo.setAccessToken(jwtUtil.getAccessToken(userId));
        return ResponseVO.success(userVo);
    }

    @PostMapping("logout")
    @Operation(summary = "退出登录",description = "退出登录")
    public ResponseVO logout(){
        Long userId = AuthContext.getId();
        //Redis删除登录信息
        redisTemplate.delete("user:"+userId);
        return ResponseVO.success();
    }
}
