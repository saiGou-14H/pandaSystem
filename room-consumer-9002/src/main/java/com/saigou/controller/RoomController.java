package com.saigou.controller;
import com.saigou.entity.Room;
import com.saigou.util.ResponseEnum;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.saigou.api.RoomApi;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/room")
@Tag(name="教室微服务")
public class RoomController {
    @Resource
    RoomApi roomApi;
    @GetMapping("list")
    @Operation(summary = "查询",description = "查询所有教室")
    public ResponseVO getAll() {
        List<Room> collect = roomApi.getAll().stream().filter(room -> room.getDeleted() == 0).collect(Collectors.toList());
        return ResponseVO.success(collect);
    }

    @PostMapping("add")
    @Operation(summary = "新增",description = "创建教室")
    public ResponseVO add(@RequestBody Room room) {
        return ResponseVO.success(roomApi.add(room));
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除",description = "删除教室")
    public ResponseVO delete(@PathVariable("id") Long id) {
        Room byId = roomApi.getById(id, null);
        if(byId==null){
            return ResponseVO.error(ResponseEnum.ERROR.getCode(),"教室不存在");
        }
        if(byId.getDeleted()==1){
            return ResponseVO.error(ResponseEnum.ERROR.getCode(),"教室已被删除");
        }
        byId.setDeleted(1);
        return ResponseVO.success(roomApi.update(byId));
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改教室信息")
    public ResponseVO update(@RequestBody Room room) {
        System.out.println(room);
        return ResponseVO.success(roomApi.update(room));
    }

    @GetMapping("get")
    @Operation(summary = "查询",description = "根据id或address查找教室")
    public ResponseVO getById(@RequestParam(name = "id",required = false) Long id,@RequestParam(name = "address",required = false) String address) {
        return ResponseVO.success(roomApi.getById(id,address));
    }

}
