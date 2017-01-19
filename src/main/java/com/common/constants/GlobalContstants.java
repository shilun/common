//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.constants;

import com.common.util.StringUtils;

public class GlobalContstants {
    public static String LOGIN_ROLE_RESOURCE_KEY = "login.role.resource.key";
    private String authKey;
    private String version;
    private boolean authHead = false;

    public GlobalContstants() {
    }

    public String getAuthKey() {
        return this.authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public boolean isAuthHead() {
        return this.authHead;
    }

    public void setAuthHead(boolean authHead) {
        this.authHead = authHead;
    }

    public String getVersion() {
        if(StringUtils.isBlank(this.version)) {
            this.version = String.valueOf(System.currentTimeMillis());
        }

        return this.version;
    }
}
