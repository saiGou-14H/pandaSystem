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

    String FACE_OUTPUT = "face_output";

    String HUMAN_OUTPUT = "human_output";

    String VEHICLE_OUTPUT = "vehicle_output";

    @Output(FACE_OUTPUT)
    MessageChannel faceOutput();

    @Output(HUMAN_OUTPUT)
    MessageChannel humanOutput();

    @Output(VEHICLE_OUTPUT)
    MessageChannel vehicleOutput();

}

