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
        * @since 2025-03-13
        */
        @Data
        @TableName("room_control")
        @ApiModel(value = "RoomControl对象", description = "")
        public class RoomControl implements Serializable {

        private static final long serialVersionUID = 1L;

        @TableId("id")
        private Long id;

        @TableField("room_id")
        private Long roomId;

        @TableField("control_id")
        private Long controlId;


        }




