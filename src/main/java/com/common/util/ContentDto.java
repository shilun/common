package com.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("参数")
public class ContentDto implements Serializable {
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
}
