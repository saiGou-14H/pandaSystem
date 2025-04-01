package com.saigou.util;

import cn.hutool.json.JSONUtil;
import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.entity.KafkaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableBinding(CustomSinkChannel.class)
public class KafkaSinkHandler {
    private final IMysqlAnalyzerService iMysqlAnalyzerService;
    @ServiceActivator(inputChannel = CustomSinkChannel.ANALYZER_INPUT)
    public void receive(Object message) {
        KafkaEntity result = JSONUtil.toBean(message.toString(), KafkaEntity.class);
        iMysqlAnalyzerService.addAnalysisResult(result.getControlId(), result.getResult());
    }

}
