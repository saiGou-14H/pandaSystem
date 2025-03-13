package com.saigou.controller;


import com.saigou.api.userApi;
import com.saigou.dto.UserDto;
import com.saigou.entity.User;
import com.saigou.util.AuthContext;
import com.saigou.util.ResponseVO;

import com.saigou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author saigou
 * @since 2024-04-19
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name="用户微服务")
@RequiredArgsConstructor
public class UserController {
    private final userApi userApi;

    @GetMapping("list")
    @Operation(summary = "查询",description = "查询所有用户")
    public ResponseVO getAll(){
        return ResponseVO.success(userApi.getAll());
    }

    @PostMapping ("add")
    @Operation(summary = "新增",description = "创建用户")
    public ResponseVO add(@RequestBody User user){
        return ResponseVO.success(userApi.add(user));
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除",description = "删除用户")
    public ResponseVO delete(@PathVariable("id") Long id){
        return ResponseVO.success(userApi.delete(id));
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改用户信息")
    public ResponseVO update(HttpServletRequest request,@RequestBody UserDto userdto){
        Long userId = AuthContext.getId();
        User user = userApi.get(userId,null);
        BeanUtils.copyProperties(userdto,user);
        return ResponseVO.success(userApi.update(user));
    }
    @GetMapping("get")
    @Operation(summary = "查询",description = "根据id或account查找用户")
    public ResponseVO get(@RequestParam(name = "id",required = false)Long id,@RequestParam(name = "account",required = false)String account){
        return ResponseVO.success(userApi.get(id,account));
    }
    @GetMapping("info")
    @Operation(summary = "获取登录用户信息",description = "获取登录信息")
    public ResponseVO info(){
        Long userId = AuthContext.getId();
        User info = userApi.get(userId,null);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(info,userVo);
        return ResponseVO.success(userVo);
    }

}
