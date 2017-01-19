//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.model;

import com.common.util.IGlossary;

public enum ContentType implements IGlossary {
    MOBILE("手机", Integer.valueOf(1)),
    EMAIL("邮件", Integer.valueOf(2)),
    WEIXIN("微信", Integer.valueOf(3));

    private String name;
    private Integer value;

    private ContentType(String name, Integer value) {
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
