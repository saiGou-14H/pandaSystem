package com.saigou.util;

import cn.hutool.json.JSONUtil;
import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.api.service.IRedisAnalyzerResultService;
import com.saigou.context.WebSocketContext;
import com.saigou.entity.KafkaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@EnableBinding(CustomSinkChannel.class)
public class KafkaSinkHandler {
    private final IMysqlAnalyzerService iMysqlAnalyzerService;
    private final WebSocketContext webSocketContext;
    private final IRedisAnalyzerResultService iRedisAnalyzerResultService;
    @ServiceActivator(inputChannel = CustomSinkChannel.ANALYZER_INPUT)
    public void receive(Object message) {
        KafkaEntity result = JSONUtil.toBean(message.toString(), KafkaEntity.class);
        iMysqlAnalyzerService.addAnalysisResult(result.getControlId(), result.getResult());
        iRedisAnalyzerResultService.addAnalysisResult2Hash(result.getControlId(), result.getResult().getTimestamp(), result.getResult());
        result.getResult().setImageData(null); // 不传输Image，减少开销
        webSocketContext.sendMessageByControlId(result.getControlId(), JSONUtil.toJsonStr(result.getResult()));
    }
}
