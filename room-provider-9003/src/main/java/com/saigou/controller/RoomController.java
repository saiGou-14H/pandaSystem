package com.saigou.controller;

import com.saigou.entity.Room;
import com.saigou.entity.User;
import com.saigou.service.impl.RoomServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/room")
@Tag(name="教室微服务")
public class RoomController {
    @Resource
    private RoomServiceImpl roomService;

    @PostMapping("add")
    @Operation(summary = "新增",description = "创建教室")
    public int add(@RequestBody Room room){
        return roomService.add(room);
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除",description = "删除教室")
    public int delete(Long id){
        return roomService.delete(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改教室信息")
    public int update(@RequestBody Room room){
        return roomService.update(room);
    }

    @GetMapping("get")
    @Operation(summary = "查询",description = "根据id或地址查找教室")
    public Room get(@RequestParam(name = "id",required = false) Long id,@RequestParam(name = "address",required = false) String address){
        if (id!=null)
            return roomService.getById(id);
        if (address!=null)
            return roomService.getByRoomAddress(address);
        return null;
    }

    @GetMapping("list")
    @Operation(summary = "查询",description = "获取教室列表")
    public List<Room> getAll(){
        return roomService.getAll();
    }

}
