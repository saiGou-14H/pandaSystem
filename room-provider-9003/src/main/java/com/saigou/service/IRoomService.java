package com.saigou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saigou.entity.Room;

import java.util.List;

public interface IRoomService extends IService<Room> {
    int add(Room room);
    int delete(Long id);
    int update(Room room);
    Room getById(Long id);
    Room getByRoomAddress(String roomAddress);
    List<Room> getAll();
}
