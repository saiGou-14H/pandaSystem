package com.saigou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(value = "ControlDto对象", description = "")
public class ControlDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("布控id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("布控推流地址")
    private String url;

    @ApiModelProperty("视频帧率")
    private Double fps;

    @ApiModelProperty("布控状态")
    private String status;

    @ApiModelProperty("布控添加时间")
    private LocalDateTime createTime;

    @ApiModelProperty("布控教室id")
    private Long roomId;
    @ApiModelProperty("布控教室名称")
    private String roomName;
    @ApiModelProperty("布控教室地址")
    private String roomAddress;
    @ApiModelProperty("布控教室描述")
    private String roomDescription;
    @ApiModelProperty("布控教室url")
    private String roomUrl;


}




