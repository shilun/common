package com.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public class PageInfo implements Serializable {

    private Integer page = 0;
    private Integer size = 10;

    public Pageable getPage() {
        return new PageRequest(page, size);
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
