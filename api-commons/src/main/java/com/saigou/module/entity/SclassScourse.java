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
        @TableName("sclass_scourse")
        @ApiModel(value = "SclassScourse对象", description = "")
        public class SclassScourse implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId("id")
        private Long id;

        @TableField("sclass_id")
        private Long sclassId;

        @TableField("scourse_id")
        private Long scourseId;


        }




