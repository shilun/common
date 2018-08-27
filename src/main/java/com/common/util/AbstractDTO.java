package com.common.util;

/**
 * 抽像dto
 */
public abstract class AbstractDTO {
    private Long id;
    private PageInfo pageinfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PageInfo getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageInfo pageinfo) {
        this.pageinfo = pageinfo;
    }
}
