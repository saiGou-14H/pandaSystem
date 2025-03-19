package com.saigou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saigou.entity.Scourse;

import java.util.List;

public interface ICourseService extends IService<Scourse> {
    public int add(Scourse scourse);
    public int delete(Long id);
    public int update(Scourse scourse);
    public Scourse getById(Integer id);
    public List<Scourse> getAll();
}
