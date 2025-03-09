package com.saigou.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author saigou
 * @since 2024-11-23
 */
@Data
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("昵称/姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("账号/学号")
    @TableField("account")
    private String account;


    @ApiModelProperty("头像url")
    @TableField("avatar")
    private String avatar;


    @ApiModelProperty("账号是否封禁")
    @TableField("isban")
    private Integer isban;


}




