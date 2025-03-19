package com.saigou.module.mapper;

import com.saigou.module.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author saigou
 * @since 2025-03-18
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
