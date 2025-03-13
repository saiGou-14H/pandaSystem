package com.saigou.module.entity;

        import com.baomidou.mybatisplus.annotation.TableField;
        import com.baomidou.mybatisplus.annotation.TableId;
        import com.baomidou.mybatisplus.annotation.TableLogic;
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
        * @since 2025-03-13
        */
        @Data
        @TableName("room")
        @ApiModel(value = "Room对象", description = "")
        public class Room implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId("id")
        private Long id;

        @ApiModelProperty("名称")
        @TableField("name")
        private String name;

        @ApiModelProperty("地址")
        @TableField("address")
        private String address;

        @ApiModelProperty("描述")
        @TableField("description")
        private String description;

        @TableField("url")
        private String url;

        @TableField("deleted")
        @TableLogic
        private Integer deleted;


        }




