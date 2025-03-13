package com.saigou.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.Room;
import com.saigou.mapper.IRoomMapper;
import com.saigou.service.IRoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends ServiceImpl<IRoomMapper, Room> implements IRoomService
{
    @Resource
    private IRoomMapper roomMapper;
    @Override
    public int add(Room room) {
        return roomMapper.insert(room);
    }

    @Override
    public int delete(Long id) {
        return roomMapper.deleteById(id);
    }

    @Override
    public int update(Room room) {
        System.out.println(room);
        return roomMapper.updateById(room);
    }

    @Override
    public Room getById(Long id) {
        return roomMapper.selectById(id);
    }

    @Override
    public Room getByRoomAddress(String roomAddress) {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        wrapper.eq("address",roomAddress);
        return roomMapper.selectOne(wrapper);
    }

    @Override
    public List<Room> getAll() {
        return roomMapper.getAll();
    }
}
