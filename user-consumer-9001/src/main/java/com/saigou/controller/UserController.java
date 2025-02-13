package com.saigou.controller;


import com.saigou.api.userApi;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
public class UserController {
    @Resource
    userApi userApi;

//    @PostMapping ("add")
//    @Operation(summary = "新增",description = "创建用户")
//    public ResponseVO add(@RequestBody User user){
//        return ResponseVO.success(userService.add(user));
//    }
//
//    @DeleteMapping("delete/{id}")
//    @Operation(summary = "删除",description = "删除用户")
//    public ResponseVO delete(@PathVariable("id") Integer id){
//        return ResponseVO.success(userService.delete(id));
//    }
//
//    @PutMapping("update")
//    @Operation(summary = "修改",description = "修改用户信息")
//    public ResponseVO update(@RequestBody UserDto userdto){
//        User user = new User();
//        BeanUtils.copyProperties(userdto,user);
//
//        return ResponseVO.success(userService.update(user));
//    }
//    @GetMapping("get/{id}")
//    @Operation(summary = "查询",description = "根据ID查找用户")
//    public ResponseVO getById(@PathVariable("id") Integer id){
//        System.out.println(id);
//        return ResponseVO.success(userService.getById(id));
//    }
    @GetMapping("all")
    @Operation(summary = "查询",description = "查询所有用户")
    public ResponseVO getAll(){
        return userApi.getAll();
    }
}
