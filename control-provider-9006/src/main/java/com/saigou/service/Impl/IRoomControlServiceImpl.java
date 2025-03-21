package com.saigou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.RoomControl;
import com.saigou.mapper.IRoomControllerMapper;
import com.saigou.service.IRoomControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IRoomControlServiceImpl extends ServiceImpl<IRoomControllerMapper, RoomControl> implements IRoomControlService {
    private final IRoomControllerMapper roomControllerMapper;
    @Override
    public int create(RoomControl roomControl) {
        return roomControllerMapper.insert(roomControl);
    }

    @Override
    public int delete(Long id) {
        QueryWrapper<RoomControl> wrapper = new QueryWrapper<RoomControl>();
        wrapper.eq("id",id);
        return roomControllerMapper.delete(wrapper);
    }

    @Override
    public int deleteByRoomId(Long roomId) {
        QueryWrapper<RoomControl> wrapper = new QueryWrapper<RoomControl>();
        wrapper.eq("room_id",roomId);
        return roomControllerMapper.delete(wrapper);
    }

    @Override
    public int deleteByControlId(Long controlId) {
        QueryWrapper<RoomControl> wrapper = new QueryWrapper<RoomControl>();
        wrapper.eq("control_id",controlId);
        return roomControllerMapper.delete(wrapper);
    }

    @Override
    public int update(RoomControl roomControl) {
        return roomControllerMapper.updateById(roomControl);
    }

    @Override
    public RoomControl getById(Long id) {
        return roomControllerMapper.selectById(id);
    }

    @Override
    public RoomControl getByControlId(Long controlId) {
        QueryWrapper wrapper = new QueryWrapper<RoomControl>();
        wrapper.eq("control_id",controlId);
        return roomControllerMapper.selectOne(wrapper);
    }

    @Override
    public List<RoomControl> getAll() {
        return roomControllerMapper.selectList(null);
    }
}
