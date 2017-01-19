package com.common.util;

import java.io.Serializable;

/**
 * Created by shilun on 17-1-19.
 */
public class MateDataInfo implements Serializable {
    private String code;
    private String name;
    private Class type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
