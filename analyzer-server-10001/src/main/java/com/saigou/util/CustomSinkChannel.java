package com.saigou.util;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel; /**
 * 自定义（消费）通道 - 模仿sink接口造轮子
 *
 * @author chenmeng
 */
public interface CustomSinkChannel {

    String FACE_INPUT = "face_input";

    String HUMAN_INPUT = "human_input";

    String VEHICLE_INPUT = "vehicle_input";

    @Input(FACE_INPUT)
    SubscribableChannel faceInput();

    @Input(HUMAN_INPUT)
    SubscribableChannel humanInput();

    @Input(VEHICLE_INPUT)
    SubscribableChannel vehicleInput();

}
