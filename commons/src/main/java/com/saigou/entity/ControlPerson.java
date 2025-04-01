package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author saigou
 * @since 2025-03-30
 */
@Data
@TableName("control_person")
@ApiModel(value = "ControlPerson对象", description = "")
public class ControlPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("person_id")
    private Long personId;

    @TableField("control_id")
    private Long controlId;

    @TableField("bbox_min_x")
    private Float bboxMinX;

    @TableField("bbox_min_y")
    private Float bboxMinY;

    @TableField("bbox_max_x")
    private Float bboxMaxX;

    @TableField("bbox_max_y")
    private Float bboxMaxY;

    @TableField("score")
    private Float score;

    @TableField("track_id")
    private Long trackId;

    @ApiModelProperty("姿态分类")
    @TableField("posture_type")
    private String postureType;

    @ApiModelProperty("扩展属性")
    @TableField("attributes")
    private String attributes;
    @ApiModelProperty("timestamp")
    @TableField("timestamp")
    private Long timestamp;

}




