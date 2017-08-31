package com.common.web;

/**
 * Created by Administrator on 2017/8/30.
 */
public class WebLoginProperties {
    private String domain;
    private String loginUrl;
    private String cookeyEncryptKey;
    private String loginCookieKey;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getCookeyEncryptKey() {
        return cookeyEncryptKey;
    }

    public void setCookeyEncryptKey(String cookeyEncryptKey) {
        this.cookeyEncryptKey = cookeyEncryptKey;
    }

    public String getLoginCookieKey() {
        return loginCookieKey;
    }

    public void setLoginCookieKey(String loginCookieKey) {
        this.loginCookieKey = loginCookieKey;
    }
}
