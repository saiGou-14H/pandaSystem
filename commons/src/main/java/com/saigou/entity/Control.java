package com.saigou.entity;

        import com.baomidou.mybatisplus.annotation.*;

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
        @TableId(value = "id", type = IdType.ASSIGN_ID)
        private Long id;

        @ApiModelProperty("布控推流地址")
        @TableField("url")
        private String url;

        @ApiModelProperty("视频帧率")
        @TableField("fps")
        private Double fps;

        @ApiModelProperty("布控状态")
        @TableField("status")
        private String status;

        @ApiModelProperty("布控添加时间")
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;

        @ApiModelProperty("逻辑删除")
        @TableField("deleted")
        private Boolean deleted;


        }




