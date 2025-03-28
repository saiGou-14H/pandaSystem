package com.saigou.util;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义（生产）通道 - 模仿source接口造轮子
 *
 * @author chenmeng
 */
public interface CustomSourceChannel {

    String ANALYZER_OUTPUT = "analyzer_topic";

    @Output(ANALYZER_OUTPUT)
    MessageChannel analyzerOutput();

}

