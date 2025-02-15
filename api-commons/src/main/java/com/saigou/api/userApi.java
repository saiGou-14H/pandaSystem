package com.saigou.api;

import com.saigou.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "user-provider-9000")
public interface userApi {
    @GetMapping("user/list")
    List<User> getAll();

    @GetMapping("user/get")
    User get(@RequestParam(name = "id",required = false)Long id,@RequestParam(name = "account",required = false)String account);

    @DeleteMapping("user/delete/{id}")
    int delete(@PathVariable("id") Long id);

    @PostMapping("user/add")
    int add(@RequestBody User user);

    @PutMapping("user/update")
    int update(@RequestBody User user);

}
