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
 * @since 2025-03-18
 */
@Data
@TableName("student_sclass")
@ApiModel(value = "StudentSclass对象", description = "")
public class StudentSclass implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("学生id")
    @TableField("student_id")
    private Long studentId;

    @ApiModelProperty("班级id")
    @TableField("sclass_id")
    private Long sclassId;


}




