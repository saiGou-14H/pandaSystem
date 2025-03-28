package com.saigou.util;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel; /**
 * 自定义（消费）通道 - 模仿sink接口造轮子
 *
 * @author chenmeng
 */
public interface CustomSinkChannel {

    String ANALYZER_INPUT = "analyzer_output";

    @Input(ANALYZER_INPUT)
    SubscribableChannel faceInput();
}
