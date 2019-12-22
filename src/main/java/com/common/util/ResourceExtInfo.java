package com.common.util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 资源类型
 */
@Data
@Api(tags = "资源")
public class ResourceExtInfo implements Serializable {
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer seq;
    /**
     * 类型 1 图片 2 语音 3 视频
     */
    @ApiModelProperty("类型 1 图片 2 语音 3 视频")
    private Integer type;
    /**
     * 资源url或内容
     */
    @ApiModelProperty("资源url或内容")
    private String content;

    @ApiModelProperty("宽度")
    private Integer width;
    @ApiModelProperty("高度")
    private Integer height;
}
