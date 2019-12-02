package com.common.util;

import com.common.util.model.OrderTypeEnum;
import lombok.Data;

@Data
public class PageInfoDto {
    /**
     * 页索引
     */
    private Integer page;

    /**
     * 页大小
     */
    private Integer size;
    /**
     * 排序列
     */
    private String orderColumn;
    /**
     * 排序类型
     */
    private OrderTypeEnum orderType;
}
