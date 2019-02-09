//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyStringUtils {
    public PropertyStringUtils() {
    }

    public static Map<String, String> getPropertys(String propertyStr) {
        HashMap propertys = new HashMap();
        if(StringUtils.isEmpty(propertyStr)) {
            return propertys;
        } else {
            String[] split = propertyStr.split(";");
            String[] var3 = split;
            int var4 = split.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String property = var3[var5];
                String[] temp = property.split(":");
                if(temp.length == 2) {
                    propertys.put(temp[0], temp[1]);
                }
            }

            return propertys;
        }
    }

    public static String setPropertyValue(String propName, String value, String content) {
        if(StringUtils.isBlank(value)) {
            return content;
        } else if(StringUtils.isBlank(propName)) {
            throw new RuntimeException("设置属性值错误，属性名称不能为空！");
        } else if(propName.indexOf(":") != -1) {
            throw new RuntimeException("设置属性值错误，属性名称不能包括冒号！");
        } else if(propName.indexOf(";") != -1) {
            throw new RuntimeException("设置属性值错误，属性名称不能包括分号！");
        } else if(value.indexOf(":") != -1) {
            throw new RuntimeException("设置属性值错误，属性值不能包括冒号！");
        } else if(value.indexOf(";") != -1) {
            throw new RuntimeException("设置属性值错误，属性值不能包括分号！");
        } else {
            Map propertys = getPropertys(content);
            propertys.put(propName, value);
            StringBuilder builder = new StringBuilder();
            Iterator var5 = propertys.keySet().iterator();

            while(var5.hasNext()) {
                String pro = (String)var5.next();
                builder.append(pro).append(":").append((String)propertys.get(pro)).append(";");
            }

            return builder.toString();
        }
    }

    public static String getPropertyValue(String propName, String content) {
        return (String)getPropertys(content).get(propName);
    }

    public static String getPropertys(Map<String, String> props) {
        StringBuilder builder = new StringBuilder();
        Iterator var2 = props.keySet().iterator();

        while(var2.hasNext()) {
            String pro = (String)var2.next();
            builder.append(pro).append(":").append((String)props.get(pro)).append(";");
        }

        return builder.toString();
    }
}
