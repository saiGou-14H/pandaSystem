package com.saigou.util;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomSinkChannel.class)
public class KafkaSinkHandler {

    @ServiceActivator(inputChannel = CustomSinkChannel.ANALYZER_INPUT)
    public void receive(Object message) {
        System.out.println("analyzer_input:" + message);
    }

    @StreamListener(CustomSinkChannel.ANALYZER_INPUT)
    public void receive2(Object message) {
        System.out.println("analyzer_input2:" + message);
    }
}
