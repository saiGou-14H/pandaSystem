package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author saigou
 * @since 2025-03-18
 */
@Data
@TableName("scourse")
@ApiModel(value = "Scourse对象", description = "")
public class Scourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty("课程名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("考试类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("描述")
    @TableField("desc")
    private String desc;

    @ApiModelProperty("开课时间")
    @TableField("start_date")
    private LocalDate startDate;

    @ApiModelProperty("结课时间")
    @TableField("end_date")
    private LocalDate endDate;

    @ApiModelProperty("上课时间")
    @TableField("datetime")
    private LocalTime datetime;

    @ApiModelProperty("考试时间")
    @TableField("exam_time")
    private LocalDateTime examTime;


}




