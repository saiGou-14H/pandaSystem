package com.saigou.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class UserVo implements Serializable{

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

        @TableField("accessToken")
        private String accessToken;


        @TableField("refreshToken")
        private String refreshToken;

}
