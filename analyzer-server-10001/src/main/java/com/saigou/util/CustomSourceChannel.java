package com.saigou.util;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义（生产）通道 - 模仿source接口造轮子
 *
 * @author chenmeng
 */
public interface CustomSourceChannel {

    String ANALYZER_OUTPUT = "analyzer_output";

    @Output(ANALYZER_OUTPUT)
    MessageChannel analyzerOutput();

}

