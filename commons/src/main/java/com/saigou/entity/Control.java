package com.saigou.entity;

        import com.baomidou.mybatisplus.annotation.TableField;
        import com.baomidou.mybatisplus.annotation.TableId;
        import com.baomidou.mybatisplus.annotation.TableName;
        import java.io.Serializable;
        import java.time.LocalDateTime;
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
        @TableName("control")
        @ApiModel(value = "Control对象", description = "")
        public class Control implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("雪花ID")
        @TableId("id")
        private Long id;

        @ApiModelProperty("视频帧率")
        @TableField("fps")
        private Double fps;

        @ApiModelProperty("布控状态")
        @TableField("status")
        private String status;

        @ApiModelProperty("布控添加时间")
        @TableField("time")
        private LocalDateTime time;

        @ApiModelProperty("逻辑删除")
        @TableField("delete")
        private Boolean delete;


        }




