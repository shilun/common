//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.annotation.MapProperty;
import com.common.exception.ApplicationException;
import com.common.util.BeanCoper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class MapPropertyUtils {
    private static Logger logger = Logger.getLogger(MapPropertyUtils.class);

    public MapPropertyUtils() {
    }

    public static Map<String, Object> toMap(Object source, Class classz) {
        if(source == null) {
            throw new ApplicationException("get source error,source can\'t be null");
        } else {
            HashMap result = null;

            try {
                result = new HashMap();
                Field[] e = classz.getDeclaredFields();
                Field[] var4 = e;
                int var5 = e.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field item = var4[var6];
                    item.setAccessible(true);
                    if(item.getAnnotation(MapProperty.class) != null) {
                        Object value = item.get(source);
                        if(value != null) {
                            result.put(item.getName(), item.get(source));
                        }
                    }
                }

                return result;
            } catch (IllegalAccessException var9) {
                logger.error("map to Object error", var9);
                throw new ApplicationException("get source field error", var9);
            }
        }
    }

    public static Map<String, Object> toMap(Object source) {
        if(source == null) {
            throw new ApplicationException("get source error,source can\'t be null");
        } else {
            HashMap result = null;

            try {
                result = new HashMap();
                Field[] e = source.getClass().getDeclaredFields();
                Field[] var3 = e;
                int var4 = e.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Field item = var3[var5];
                    item.setAccessible(true);
                    if(item.getAnnotation(MapProperty.class) != null) {
                        Object value = item.get(source);
                        if(value != null) {
                            result.put(item.getName(), item.get(source));
                        }
                    }
                }

                return result;
            } catch (IllegalAccessException var8) {
                logger.error("map to Object error", var8);
                throw new ApplicationException("get source field error", var8);
            }
        }
    }

    public static <T> T toObject(Map<String, Object> values, Class<T> target) {
        if(values == null) {
            throw new ApplicationException("get object error");
        } else {
            Object t = null;

            try {
                t = target.newInstance();
                new HashMap();
                Field[] fields = target.getDeclaredFields();
                Field[] var5 = fields;
                int var6 = fields.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Field item = var5[var7];
                    if(item.getAnnotation(MapProperty.class) != null) {
                        Object o = values.get(item.getName());
                        if(o != null) {
                            BeanCoper.setProperty(t, item.getName(), o);
                        }
                    }
                }

                return (T) t;
            } catch (Exception var10) {
                logger.error("map to Object error", var10);
                throw new ApplicationException("get object error");
            }
        }
    }
}
