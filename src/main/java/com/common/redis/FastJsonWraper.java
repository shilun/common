package com.common.redis;

/**
 * @author liuyazhuang
 * @date 2018/8/15 23:15
 * @description FastJsonWraper包装类
 * @version 1.0.0
 */
public class FastJsonWraper<T> {
    private T value;
 
    public FastJsonWraper() {
    }
 
    public FastJsonWraper(T value) {
        this.value = value;
    }
 
    public T getValue() {
        return value;
    }
 
    public void setValue(T value) {
        this.value = value;
    }
}