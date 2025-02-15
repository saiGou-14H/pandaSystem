package com.saigou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saigou.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    @Select("select * from room;")
    List<Room> getAll();
}
