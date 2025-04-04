package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.saigou.config.MultilingualMapTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@TableName(value = "control_timestamp",autoResultMap = true) // autoResultMap设置为true,否则获取json无法自动转成Map类型，则返回为null
@ApiModel(value = "ControlTimestamp对象", description = "")
public class ControlTimestamp implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("control_id")
    private Long controlId;

    @TableField("timestamp")
    private Long timestamp;

    @TableField("total_score")
    private Float totalScore;

    @TableField("status")
    private String status;

    @TableField("alert")
    private String alert;

    @TableField(value = "emotion_distribution",typeHandler = MultilingualMapTypeHandler.class)
    private Map<String, Integer> emotionDistribution;

    @TableField(value = "posture_distribution",typeHandler = MultilingualMapTypeHandler.class)
    private Map<String, Integer> postureDistribution;
}
