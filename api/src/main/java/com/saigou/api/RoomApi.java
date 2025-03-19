package com.saigou.api;

import com.saigou.entity.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "room-provider-9003")
public interface RoomApi {

    @PostMapping("room/add")
    int add(@RequestBody Room room);

    @DeleteMapping("room/delete/{id}")
    int delete(@PathVariable("id") Long id);

    @PutMapping("room/update")
    int update(@RequestBody Room room);

    @GetMapping("room/list")
    List<Room> getAll();

    @GetMapping("room/get")
    Room getById(@RequestParam(name = "id",required = false) Long id,@RequestParam(name = "address",required = false) String address);
}
