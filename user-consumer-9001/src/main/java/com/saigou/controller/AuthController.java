package com.saigou.controller;

import com.saigou.api.userApi;
import com.saigou.entity.User;
import com.saigou.util.JwtUtil;
import com.saigou.util.RedisUtil;
import com.saigou.util.ResponseEnum;
import com.saigou.util.ResponseVO;
import com.saigou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
@Tag(name="登录微服务")
public class AuthController {
    @Resource
    userApi userApi;
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
        userVo.setAccessToken(JwtUtil.getAccessToken(auth.getId()));
        userVo.setRefreshToken(JwtUtil.getRefreshToken(auth.getId()));
        System.out.println("准备连接redies");
        //Redis保存token
        RedisUtil.set("user:"+auth.getId(),userVo.getRefreshToken(),60*60*1);
        System.out.println("连接redis成功");
        return ResponseVO.success(userVo);
    }
    @GetMapping("refresh")
    @Operation(summary = "刷新token",description = "刷新token")
    public ResponseVO refresh(HttpServletRequest request){
        Long userId = JwtUtil.getUserId(request);
        UserVo userVo = new UserVo();
        userVo.setAccessToken(JwtUtil.getAccessToken(userId));
        return ResponseVO.success(userVo);
    }

    @PostMapping("logout")
    @Operation(summary = "退出登录",description = "退出登录")
    public ResponseVO logout(HttpServletRequest request){
        Long userId = JwtUtil.getUserId(request);
        //Redis删除登录信息
        RedisUtil.del("user:"+userId);
        return ResponseVO.success();
    }
}
