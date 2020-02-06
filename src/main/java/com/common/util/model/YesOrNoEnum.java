//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.model;

import com.common.util.IGlossary;

public enum YesOrNoEnum implements IGlossary {
    YES("是", Integer.valueOf(1)),
    NO("否", Integer.valueOf(2));

    private String name;
    private Integer value;

    YesOrNoEnum(String name, Integer value) {
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
