package com.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("id参数")
public class IdDto implements Serializable {
    /**
     * 标识
     */
    @ApiModelProperty("id")
    private String id;
}
