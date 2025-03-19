package com.saigou.module.service.impl;

import com.saigou.module.entity.Student;
import com.saigou.module.mapper.StudentMapper;
import com.saigou.module.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author saigou
 * @since 2025-03-18
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
