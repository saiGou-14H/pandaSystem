package com.saigou.api;

import com.saigou.entity.Control;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "control-provider-9006")
public interface ControlApi {
    @DeleteMapping("/control/get/{id}")
    Control getById(@PathVariable("id") Long id);
    @PostMapping("/control/add")
    int add(@RequestBody Control control);
    @DeleteMapping("/control/delete/{id}")
    int delete(@PathVariable("id") Long id);
    @PutMapping("/control/update")
    int update(@RequestBody Control control);
    @GetMapping("/control/all")
    List<Control> getAll();

}
