package com.saigou.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Blob;

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
@TableName("control_face")
@ApiModel(value = "ControlFace对象", description = "")
public class ControlFace implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("face_id")
    private Long faceId;

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

    @ApiModelProperty("检测置信度0-1")
    @TableField("score")
    private Float score;

    @TableField("track_id")
    private Integer trackId;

    @ApiModelProperty("表情分类")
    @TableField("expression_type")
    private String expressionType;

    @ApiModelProperty("512维特征向量")
    @TableField("feature_vector")
    private Blob featureVector;

    @ApiModelProperty("扩展属性")
    @TableField("attributes")
    private String attributes;


}




