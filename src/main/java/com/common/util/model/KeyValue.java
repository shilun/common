//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class KeyValue<K, V> implements Serializable {
    private static final long serialVersionUID = -5738449733117179721L;
    @ApiModelProperty("键")
    private K key;
    @ApiModelProperty("值")
    private V value;

    public KeyValue() {
    }

    public static KeyValue<String, Integer> instance(String key, Integer value) {
        KeyValue keyValue = new KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(value);
        return keyValue;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
