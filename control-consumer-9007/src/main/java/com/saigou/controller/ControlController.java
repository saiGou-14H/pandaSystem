package com.saigou.controller;

import com.saigou.api.ControlApi;
import com.saigou.entity.Control;
import com.saigou.util.ResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("control")
@RequiredArgsConstructor
public class ControlController {
    private final ControlApi controlApi;

    @GetMapping("get/{id}")
    public ResponseVO getById(Long id) {
        return ResponseVO.success(controlApi.getById(id));
    }

    @GetMapping("all")
    public ResponseVO getAll() {
        return ResponseVO.success(controlApi.getAll());
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody Control control) {
        return ResponseVO.success(controlApi.add(control));
    }

    @DeleteMapping("delete/{id}")
    public ResponseVO delete(Long id) {
        return ResponseVO.success(controlApi.delete(id));
    }

    @PutMapping("update")
    public ResponseVO update(@RequestBody Control control) {
        return ResponseVO.success(controlApi.update(control));
    }


}
