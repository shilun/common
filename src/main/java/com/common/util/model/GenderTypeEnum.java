//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.model;

import com.common.util.IGlossary;

public enum GenderTypeEnum implements IGlossary {
    MALE("男"),
    FEMALE("女");

    private String name;
    private Integer value;

    GenderTypeEnum(String name){
        this.name=name;
    }


    public String getName() {
        return this.name;
    }

}
