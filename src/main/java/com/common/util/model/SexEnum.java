//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.model;

import com.common.util.IGlossary;

public enum SexEnum implements IGlossary {
    MALE("男", Integer.valueOf(1)),
    FEMALE("女", Integer.valueOf(2));

    private String name;
    private Integer value;

    public static SexEnum nameOf(String name) {
        SexEnum[] sexEnums = values();
        SexEnum[] var2 = sexEnums;
        int var3 = sexEnums.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SexEnum sexEnum = var2[var4];
            if(sexEnum.getName().equals(name)) {
                return sexEnum;
            }
        }

        return null;
    }

    private SexEnum(String name, Integer value) {
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
