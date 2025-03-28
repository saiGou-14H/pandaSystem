package com.saigou.api.service.Impl;

import com.saigou.api.service.IAnalyzerService;
import com.saigou.context.AnalyzerContext;
import com.saigou.entity.StreamProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyzerServiceImpl implements IAnalyzerService {

    private final AnalyzerContext analyzerContext;
    @Override
    public StreamProcessor create(Long id,String url,String stream) {
        return analyzerContext.addStreamProcessor(id,url,stream);
    }

    @Override
    public void remove(Long id) {
        analyzerContext.removeStreamProcessor(id);
    }

    @Override
    public void execute(Long id) {
        analyzerContext.executeStreamProcessor(id);
    }

    @Override
    public void cancel(Long id) {
        analyzerContext.cancelStreamProcessor(id);
    }

    @Override
    public StreamProcessor getById(Long id) {
        return analyzerContext.getStreamProcessor(id);
    }

    @Override
    public List<StreamProcessor> getAll() {
        return analyzerContext.getAllStreamProcessor();
    }
}
