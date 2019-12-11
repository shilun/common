package com.common.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 抽像dto
 */
@Data
public abstract class AbstractDTO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("分页信息")
    private PageInfo pageinfo;
}
