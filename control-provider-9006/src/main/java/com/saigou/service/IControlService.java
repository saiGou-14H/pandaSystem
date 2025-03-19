package com.saigou.service;

import com.saigou.entity.Control;

import java.util.List;

public interface IControlService {
    int add(Control control);
    int delete(Long id);
    int update(Control control);
    Control getById(Long id);
    List<Control> getAll();
}
