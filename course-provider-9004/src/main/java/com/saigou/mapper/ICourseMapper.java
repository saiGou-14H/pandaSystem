package com.saigou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saigou.entity.Scourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ICourseMapper extends BaseMapper<Scourse> {

    @Select("select * from scourse")
    public List<Scourse> getAll();
}
