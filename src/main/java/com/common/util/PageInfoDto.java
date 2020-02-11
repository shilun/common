package com.common.util;

import com.common.util.model.OrderTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
@ApiModel("分页对象")
public class PageInfoDto implements Serializable {
    /**
     * 页索引
     */
    @ApiModelProperty("页码")
    private Integer page;

    /**
     * 页大小
     */
    @ApiModelProperty("页面大小")
    private Integer size;
    /**
     * 排序列
     */
    @ApiModelProperty("排序列")
    private String orderColumn = "createTime";
    /**
     * 排序类型
     */
    @ApiModelProperty("排序类型")
    private OrderTypeEnum orderType = OrderTypeEnum.DESC;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public Pageable getPageable() {
        if (page == null) {
            page = 0;
        }
        if (size == null || size.intValue() == 0) {
            size = 10;
        }
        return PageRequest.of(page, size);
    }
}