package com.saigou.api;

import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "control-provider-9006")
public interface ControlApi {
    @GetMapping("/control/get/{id}")
    ControlDto getById(@PathVariable("id") Long id);
    @PostMapping("/control/create")
    Long create(@RequestBody ControlDto controlDto);
    @DeleteMapping("/control/delete/{id}")
    int delete(@PathVariable("id") Long id);
    @PutMapping("/control/update")
    int update(@RequestBody Control control);
    @GetMapping("/control/list")
    List<ControlDto> getAll();
    @GetMapping("/control/execute/{id}")
    int executeControl(@PathVariable("id") Long id);
    @GetMapping("/control/cancel/{id}")
    int cancelControl(@PathVariable("id") Long id);

}
