package com.common.util;

import com.common.util.model.OrderTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageInfoDto implements Serializable {
    /**
     * 页索引
     */
    private Integer page = 0;

    /**
     * 页大小
     */
    private Integer size = 10;
    /**
     * 排序列
     */
    private String orderColumn = "createTime";
    /**
     * 排序类型
     */
    private OrderTypeEnum orderType = OrderTypeEnum.DESC;
}
