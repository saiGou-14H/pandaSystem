package com.saigou.api.service;

import com.saigou.entity.StreamProcessor;

import java.util.List;

public interface IAnalyzerService {

    StreamProcessor create(Long id,String url,String stream);

    void remove(Long id);

    void execute(Long id);

    void cancel(Long id);
    StreamProcessor getById(Long id);

    List<StreamProcessor> getAll();
}
