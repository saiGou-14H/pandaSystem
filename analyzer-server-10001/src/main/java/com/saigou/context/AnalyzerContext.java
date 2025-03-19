package com.saigou.context;

import com.saigou.util.StreamProcessor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Data
@Component
public class AnalyzerContext {
    private final String ANALYZER_CONTEXT_KEY = "analyzerContext";
    private final Map<Long, StreamProcessor> streamProcessorMap = new HashMap<>();

    public StreamProcessor getStreamProcessor(Long id) {
        return streamProcessorMap.get(id);
    }

    public void addStreamProcessor(Long id, StreamProcessor streamProcessor) {
        streamProcessorMap.put(id, streamProcessor);
    }

    public void removeStreamProcessor(Long id) {
        streamProcessorMap.remove(id);
    }

    public void clear() {
        streamProcessorMap.clear();
    }
    public void stopAll() {
        streamProcessorMap.forEach((k, v) -> v.stop());
    }
    public void startAll() {
        streamProcessorMap.forEach((k, v) -> v.start());
    }
}
