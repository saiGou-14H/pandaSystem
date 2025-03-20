package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class User implements Serializable {

private static final long serialVersionUID = 1L;

@ApiModelProperty("雪花ID")
@TableId(value = "id", type = IdType.ASSIGN_ID)
private Long id;

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

@ApiModelProperty("密码")
@TableField("password")
private String password;

@ApiModelProperty("上一次登录ip")
@TableField("login_ip")
private String loginIp;

@ApiModelProperty("头像url")
@TableField("avatar")
private String avatar;

@ApiModelProperty("创建时间")
@TableField("create_date")
private LocalDateTime createDate;

@ApiModelProperty("账号是否封禁")
@TableField("isban")
private Integer isban;


}




