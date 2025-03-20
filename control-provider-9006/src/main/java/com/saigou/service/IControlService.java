package com.saigou.service;

import com.saigou.entity.Control;
import com.saigou.entity.ControlDto;

import java.util.List;

public interface IControlService {
    int add(Control control);
    int delete(Long id);
    int update(Control control);
    ControlDto getById(Long id);
    List<ControlDto> getAll();
    int executeControl(Long id);
    int cancelControl(Long id);
}
