package com.saigou.module.entity;

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
        @TableName("sclass")
        @ApiModel(value = "Sclass对象", description = "")
        public class Sclass implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId("id")
        private Long id;

        @TableField("name")
        private String name;


        }




