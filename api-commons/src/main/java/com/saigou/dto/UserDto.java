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
 * @since 2024-04-19
 */
@Data
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @ApiModelProperty("雪花id")
    @TableField("uuid")
    private Integer uuid;

    @ApiModelProperty("昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("邮箱")
    @TableField("mail")
    private String mail;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("登录IP地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty("头像地址")
    @TableField("head_url")
    private String headUrl;

    @ApiModelProperty("账号创建时间")
    @TableField("create_data")
    private LocalDateTime createData;

    @ApiModelProperty("是否封禁（1封禁）")
    @TableField("ban")
    private Integer ban;


}




