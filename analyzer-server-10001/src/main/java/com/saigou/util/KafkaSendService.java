package com.saigou.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {CustomSourceChannel.class})
public class KafkaSendService {
    @Autowired
    private CustomSourceChannel customSourceChannel;
    public void send(Object message) {
        customSourceChannel.analyzerOutput().send(MessageBuilder.withPayload(message).build());
    }
}
