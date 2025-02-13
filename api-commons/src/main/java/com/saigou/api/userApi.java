package com.saigou.api;

import com.saigou.util.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@FeignClient(value = "provider-user-9000")
public interface userApi {
    @GetMapping("user/all")
    public ResponseVO  getAll();
}
