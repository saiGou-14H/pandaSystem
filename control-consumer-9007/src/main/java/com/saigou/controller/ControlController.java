package com.saigou.controller;

import com.saigou.api.AnalyzerApi;
import com.saigou.api.ControlApi;
import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import com.saigou.util.ResponseVO;
import com.saigou.vo.StreamProcessorVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
@Tag(name="布控微服务")
public class ControlController {
    private final ControlApi controlApi;
    private final AnalyzerApi analyzerApi;
    private final String APP = "analyzer";

    @GetMapping("/get/{id}")
    @Operation(summary = "根据id查询布控信息",description = "根据id查询布控信息")
    public ResponseVO getById(@PathVariable("id") Long id){
        return ResponseVO.success(controlApi.getById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有布控信息",description = "查询所有布控信息")
    public ResponseVO getAll() {
        return ResponseVO.success(controlApi.getAll());
    }

    @PostMapping("/create")
    @Operation(summary = "添加布控信息",description = "添加布控信息")
    @Transactional
    public ResponseVO create(@RequestBody ControlDto controlDto) {
        Long id = controlApi.create(controlDto);
        StreamProcessorVO streamProcessorVO = analyzerApi.create(id, controlDto.getRoomUrl(), id.toString());
        controlDto.setUrl(streamProcessorVO.getHttpPushUrl());
        Control control = new Control();
        BeanUtils.copyProperties(controlDto,control);
        control.setId(id);
        return ResponseVO.success(controlApi.update(control));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除布控信息",description = "根据id删除布控信息")
    @Transactional
    public ResponseVO delete(@PathVariable("id") Long id){
        analyzerApi.remove(id);
        return ResponseVO.success(controlApi.delete(id));
    }

    @PutMapping("/update")
    @Operation(summary = "修改布控信息",description = "修改布控信息")
    public ResponseVO update(@RequestBody Control control) {
        return ResponseVO.success(controlApi.update(control));
    }

    @GetMapping("/execute/{id}")
    @Operation(summary = "布控执行",description = "布控执行")
    @Transactional
    public ResponseVO executeControl(@PathVariable("id") Long id) {
        StreamProcessorVO byId = analyzerApi.getById(id);
        if(byId==null){
            ControlDto control = controlApi.getById(id);
            analyzerApi.create(control.getId(),control.getRoomUrl(),control.getId().toString());
            analyzerApi.execute(id);
        }
        analyzerApi.execute(id);
        return ResponseVO.success(controlApi.executeControl(id));
    }

    @GetMapping("/cancel/{id}")
    @Operation(summary = "布控取消",description = "布控取消")
    public ResponseVO cancelControl(@PathVariable("id") Long id) {
        analyzerApi.cancel(id);
        return ResponseVO.success(controlApi.cancelControl(id));
    }


}
