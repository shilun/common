package com.common.codetable;

import java.io.Serializable;

/**
 * Created by shilun on 17-1-19.
 */
public class ValuePare implements Serializable {
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
