//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.util.*;
import java.util.Map.Entry;

public class ConvertMapkey {
    public ConvertMapkey() {
    }

    public static Map<String, Object> keyToLower(Map<String, Object> map) {
        HashMap r = new HashMap();
        if(map != null && map.size() != 0) {
            Iterator var2 = map.entrySet().iterator();

            while(var2.hasNext()) {
                Entry entry = (Entry)var2.next();
                r.put(((String)entry.getKey()).toLowerCase(), entry.getValue());
            }

            return r;
        } else {
            return r;
        }
    }

    public static List<Map<String, Object>> listKeyToLower(List<Map<String, Object>> listmap) {
        ArrayList r = new ArrayList();
        if(listmap != null && listmap.size() != 0) {
            Iterator var2 = listmap.iterator();

            while(var2.hasNext()) {
                Map map = (Map)var2.next();
                r.add(keyToLower(map));
            }

            return r;
        } else {
            return r;
        }
    }
}
