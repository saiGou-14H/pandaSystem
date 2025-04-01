package com.saigou.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saigou.entity.ControlPerson;
import com.saigou.entity.PersonBox;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPersonBoxMapper extends BaseMapper<ControlPerson> {
}
