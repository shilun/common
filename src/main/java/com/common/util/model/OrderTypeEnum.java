package com.common.util.model;

import com.common.util.IGlossary;

/**
 * 排序方式
 */
public enum OrderTypeEnum implements IGlossary {
    ASC("升序", 1),
    DESC("降序", 2);

    private String name;
    private Integer value;

    OrderTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }
}