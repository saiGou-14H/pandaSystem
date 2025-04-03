package com.saigou.util;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public class MultilingualMapTypeHandler extends AbstractJsonTypeHandler<Map<String, Integer>> {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Override
    public Map<String, Integer> parse(String json) {
        try {
            return objectMapper.readValue(json, 
                new TypeReference<LinkedHashMap<String, Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析失败: " + json, e);
        }
    }

    @Override
    public String toJson(Map<String, Integer> obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败", e);
        }
    }
}