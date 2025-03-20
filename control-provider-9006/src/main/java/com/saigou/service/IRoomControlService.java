package com.saigou.service;

import com.saigou.entity.RoomControl;

import java.util.List;

public interface IRoomControlService{
    int add(RoomControl roomControl);
    int delete(Long id);
    int deleteByRoomId(Long roomId);
    int deleteByControlId(Long controlId);
    int update(RoomControl roomControl);
    RoomControl getById(Long id);
    RoomControl getByControlId(Long controlId);
    List<RoomControl> getAll();
}
