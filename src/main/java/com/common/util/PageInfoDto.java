package com.common.util;

import com.common.util.model.OrderTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Data
@ApiModel("分页对象")
public class PageInfoDto implements Serializable {
    /**
     * 页索引
     */
    @ApiModelProperty("页码")
    private Integer page = 0;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小", example = "10")
    private Integer size = 10;
    /**
     * 排序列
     */
    @ApiModelProperty("排序列")
    private String orderColumn;
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
        Sort sort = Sort.unsorted();
        if (StringUtils.isBlank(orderColumn)) {
            orderColumn = "_id";
        }
        if (orderType == OrderTypeEnum.DESC) {
            sort = Sort.by(Sort.Direction.DESC, orderColumn);
        }

        return PageRequest.of(page, size, sort);
    }
}