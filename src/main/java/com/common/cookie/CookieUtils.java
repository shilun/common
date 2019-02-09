//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CookieUtils {
    private static final Log log = LogFactory.getLog(CookieUtils.class);
    private Map<String, CookieItem> cookieMap;

    public CookieUtils() {
    }

    public String getCookieValue(HttpServletRequest servletRequest, String name) {
        Cookie[] cookies = servletRequest.getCookies();
        if(cookies != null && cookies.length > 0) {
            Cookie[] var4 = cookies;
            int var5 = cookies.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Cookie cookie = var4[var6];
                String cookieName = cookie.getName();
                if(cookieName.equals(name)) {
                    if(this.cookieMap != null && this.cookieMap.containsKey(name)) {
                        CookieItem jdCookie = (CookieItem)this.cookieMap.get(name);
                        return jdCookie.getValue(cookie.getValue());
                    }

                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public void deleteCookie(HttpServletResponse servletResponse, String name) {
        Cookie cookie;
        if(this.cookieMap != null && this.cookieMap.containsKey(name)) {
            CookieItem jdCookie = (CookieItem)this.cookieMap.get(name);
            jdCookie.setExpiry(0);
            cookie = jdCookie.newCookie((String)null);
        } else {
            cookie = new Cookie(name, (String)null);
        }

        servletResponse.addCookie(cookie);
    }

    public void setCookie(HttpServletResponse servletResponse, String name, String value) {
        if(this.cookieMap != null && this.cookieMap.containsKey(name)) {
            CookieItem jdCookie = (CookieItem)this.cookieMap.get(name);
            Cookie cookie = jdCookie.newCookie(value);
            servletResponse.addCookie(cookie);
        } else {
            throw new RuntimeException("Cookie " + name + " is undefined!");
        }
    }

    public void setCookies(List<CookieItem> cookies) {
        if(cookies != null) {
            HashMap jdCookieHashMap = new HashMap(cookies.size());
            Iterator var3 = cookies.iterator();

            while(var3.hasNext()) {
                CookieItem jdCookie = (CookieItem)var3.next();
                jdCookieHashMap.put(jdCookie.getName(), jdCookie);
            }

            this.cookieMap = jdCookieHashMap;
        }

    }

    public void invalidate(HttpServletRequest request, HttpServletResponse response) {
        if(this.cookieMap != null && this.cookieMap.size() > 0) {
            Iterator var3 = this.cookieMap.entrySet().iterator();

            while(var3.hasNext()) {
                Entry entry = (Entry)var3.next();
                String key = (String)entry.getKey();
                CookieItem jdCookie = (CookieItem)entry.getValue();
                if(jdCookie.getExpiry() < 1 && StringUtils.isNotEmpty(this.getCookieValue(request, key))) {
                    this.deleteCookie(response, key);
                }
            }
        }

    }
}
