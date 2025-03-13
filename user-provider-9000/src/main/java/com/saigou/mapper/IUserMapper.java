package com.saigou.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.saigou.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author saigou
 * @since 2024-04-19
 */
@Mapper
public interface IUserMapper extends BaseMapper<User> {
    @Select("select * from user;")
    List<User> getAll();
}
