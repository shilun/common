package com.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@ApiModel("分页参数")
public class PageInfo implements Serializable {
    @ApiModelProperty("页码")
    private Integer page = 0;
    @ApiModelProperty("页大小")
    private Integer size = 10;

    public Pageable getPage() {
        return PageRequest.of(page, size);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
