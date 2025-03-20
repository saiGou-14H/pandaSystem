package com.saigou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IControllerMapper extends BaseMapper<Control> {
    @Select("SELECT control.id,control.url,control.fps,control.`status`,control.create_time,room.id AS roomId,room.`name` AS roomName,room.address AS roomAddress,room.`description` AS roomDescription,room.`url` AS roomUrl FROM control INNER JOIN room ,room_control WHERE control.id=room_control.control_id AND room_control.room_id=room.id and control.id=#{id}")
    public ControlDto getById(Long id);
    @Select("SELECT control.id,control.url,control.fps,control.`status`,control.create_time,room.id AS roomId,room.`name` AS roomName,room.address AS roomAddress,room.`description` AS roomDescription,room.`url` AS roomUrl FROM control INNER JOIN room ,room_control WHERE control.id=room_control.control_id AND room_control.room_id=room.id")
    public List<ControlDto> getAll();
}
