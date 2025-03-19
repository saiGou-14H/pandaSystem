package com.saigou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.Scourse;
import com.saigou.mapper.ICourseMapper;
import com.saigou.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl extends ServiceImpl<ICourseMapper, Scourse> implements ICourseService {
    private final ICourseMapper courseMapper;
    public int add(Scourse scourse) {
        return courseMapper.insert(scourse);
    }
    public int delete(Long id) {
        return courseMapper.deleteById(id);
    }
    public int update(Scourse scourse) {
        return courseMapper.updateById(scourse);
    }
    public Scourse getById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<Scourse> getAll() {
        return courseMapper.getAll();
    }


}