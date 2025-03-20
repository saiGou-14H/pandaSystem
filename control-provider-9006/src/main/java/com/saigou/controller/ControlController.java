package com.saigou.controller;

import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import com.saigou.entity.RoomControl;
import com.saigou.service.IControlService;
import com.saigou.service.IRoomControlService;
import com.saigou.util.IdGeneratorSnowflake;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
@Tag(name="布控微服务")
public class ControlController {
    private final IControlService iControlService;
    private final IRoomControlService iRoomControlService;

    @PostMapping("/add")
    @Operation(summary = "添加布控",description = "添加布控")
    @Transactional
    public int add(@RequestBody ControlDto controlDto){
        Control control = new Control();
        BeanUtils.copyProperties(controlDto,control);
        iControlService.add(control);
        RoomControl roomControl = new RoomControl();
        roomControl.setRoomId(controlDto.getRoomId());
        roomControl.setControlId(control.getId());
        return iRoomControlService.add(roomControl);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除",description = "删除布控")
    @Transactional
    public int delete(@PathVariable("id") Long id){
        iRoomControlService.deleteByControlId(id);
        return iControlService.delete(id);
    }

    @PutMapping("/update")
    @Operation(summary = "修改",description = "修改布控")
    public int update(@RequestBody Control control){
        return iControlService.update(control);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "查询",description = "根据id查询布控")
    public ControlDto getById(@PathVariable("id") Long id){
        return iControlService.getById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有",description = "查询所有布控")
    public List<ControlDto> getAll(){
        return iControlService.getAll();
    }

    @GetMapping("/execute/{id}")
    @Operation(summary = "执行布控",description = "执行布控")
    public int executeControl(@PathVariable("id") Long id){
        return iControlService.executeControl(id);
    }

    @GetMapping("/cancel/{id}")
    @Operation(summary = "取消布控",description = "取消布控")
    public int cancelControl(@PathVariable("id") Long id){
        return iControlService.cancelControl(id);
    }


}
