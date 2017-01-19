//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.util.HashMap;
import java.util.Map;

public class UrlUtiles {
    public UrlUtiles() {
    }

    public static String getUrlParams(String url, String name) {
        UrlUtiles urlUtiles = new UrlUtiles();
        Map map = urlUtiles.getQueryMap(url);
        return (String)map.get(name);
    }

    public Map<String, String> getQueryMap(String url) {
        int index = url.indexOf(63);
        if(index < 0) {
            return null;
        } else {
            url = url.substring(index + 1);
            String[] params = url.split("&");
            HashMap queryMap = new HashMap();
            String[] var5 = params;
            int var6 = params.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String param = var5[var7];
                String[] query = param.split("=");
                queryMap.put(query[0], query[1]);
            }

            return queryMap;
        }
    }
}
