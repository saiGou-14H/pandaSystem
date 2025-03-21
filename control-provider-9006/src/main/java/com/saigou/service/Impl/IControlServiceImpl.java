package com.saigou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;
import com.saigou.mapper.IControllerMapper;
import com.saigou.service.IControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class IControlServiceImpl  extends ServiceImpl<IControllerMapper, Control> implements IControlService{
    private final IControllerMapper iControllerMapper;

    @Override
    public int create(Control control) {
        return iControllerMapper.insert(control);
    }

    @Override
    public int delete(Long id) {
        QueryWrapper<Control> wrapper = new QueryWrapper<Control>();
        wrapper.eq("id",id);
        return iControllerMapper.delete(wrapper);
    }

    @Override
    public int update(Control control) {
        return iControllerMapper.updateById(control);
    }

    @Override
    public ControlDto getById(Long id) {
        return iControllerMapper.getById(id);
    }

    @Override
    public List<ControlDto> getAll() {
        return iControllerMapper.getAll();
    }

    @Override
    public int executeControl(Long id) {
        UpdateWrapper<Control> wrapper = new UpdateWrapper<Control>();
        wrapper.eq("id",id).set("status","active");
        return iControllerMapper.update(wrapper);
    }

    @Override
    public int cancelControl(Long id) {
        UpdateWrapper<Control> wrapper = new UpdateWrapper<Control>();
        wrapper.eq("id",id).set("status","cancel");
        return iControllerMapper.update(wrapper);
    }
}
