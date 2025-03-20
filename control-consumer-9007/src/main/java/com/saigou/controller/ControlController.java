package com.saigou.controller;

import com.saigou.api.ControlApi;
import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
@Tag(name="布控微服务")
public class ControlController {
    private final ControlApi controlApi;

    @GetMapping("/get/{id}")
    @Operation(summary = "根据id查询布控信息",description = "根据id查询布控信息")
    public ResponseVO getById(Long id) {

        return ResponseVO.success(controlApi.getById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有布控信息",description = "查询所有布控信息")
    public ResponseVO getAll() {
        return ResponseVO.success(controlApi.getAll());
    }

    @PostMapping("/add")
    @Operation(summary = "添加布控信息",description = "添加布控信息")
    public ResponseVO add(@RequestBody ControlDto controlDto) {
        return ResponseVO.success(controlApi.add(controlDto));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除布控信息",description = "根据id删除布控信息")
    public ResponseVO delete(Long id) {
        return ResponseVO.success(controlApi.delete(id));
    }

    @PutMapping("/update")
    @Operation(summary = "修改布控信息",description = "修改布控信息")
    public ResponseVO update(@RequestBody Control control) {
        return ResponseVO.success(controlApi.update(control));
    }

    @GetMapping("/execute/{id}")
    @Operation(summary = "布控执行",description = "布控执行")
    public ResponseVO executeControl(@PathVariable("id") Long id) {
        return ResponseVO.success(controlApi.executeControl(id));
    }

    @GetMapping("/cancel/{id}")
    @Operation(summary = "布控取消",description = "布控取消")
    public ResponseVO cancelControl(@PathVariable("id") Long id) {
        return ResponseVO.success(controlApi.cancelControl(id));
    }


}
