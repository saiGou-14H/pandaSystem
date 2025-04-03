package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.saigou.util.MultilingualMapTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@TableName("control_timestamp")
@ApiModel(value = "ControlTimestamp对象", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class ControlTimestamp implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private long id;

    @TableField("control_id")
    private long controlId;

    @TableField("timestamp")
    private long timestamp;

    @TableField("total_score")
    private double totalScore;

    @TableField("status")
    private String status;

    @TableField("alert")
    private String alert;

    @TableField(value = "emotion_distribution",typeHandler = MultilingualMapTypeHandler.class)
    private Map<String, Integer> emotionDistribution;

    @TableField(value = "posture_distribution",typeHandler = MultilingualMapTypeHandler.class)
    private Map<String, Integer> postureDistribution;
}
