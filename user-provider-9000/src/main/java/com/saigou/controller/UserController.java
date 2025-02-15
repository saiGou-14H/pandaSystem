package com.saigou.controller;


import com.saigou.dto.UserDto;
import com.saigou.entity.User;
import com.saigou.service.impl.UserServiceImpl;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class UserController {
    @Resource
    UserServiceImpl userService;
    @PostMapping ("add")
    @Operation(summary = "新增",description = "创建用户")
    public int add(@RequestBody User user){
        return userService.add(user);
    }
    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除",description = "删除用户")
    public int delete(@PathVariable("id") Long id){
        return userService.delete(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改用户信息")
    public int update(@RequestBody User user){
        return userService.update(user);
    }
    @GetMapping("get")
    @Operation(summary = "查询",description = "根据xx查找用户")
    public User get(@RequestParam(name = "id",required = false)Long id,@RequestParam(name = "account",required = false)String account){
        if (account!=null)
            return userService.getByAccount(account);
        if (id!=null)
            return userService.getById(id);;
        return null;
    }
    @GetMapping("list")
    @Operation(summary = "查询",description = "查询所有用户")
    public List<User> getAll(){
        return userService.getAll();
    }
}
