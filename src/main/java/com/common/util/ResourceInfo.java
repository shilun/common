package com.common.util;

import com.common.codetable.ValuePare;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 资源类型
 */
@Data
@Api(tags = "资源")
public class ResourceInfo implements Serializable {
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
     * 扩展属性
     */
    private String extendsParams;
    /**
     * 资源url或内容
     */
    @ApiModelProperty("资源url或内容")
    private String content;
}
