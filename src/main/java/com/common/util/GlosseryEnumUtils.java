//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class GlosseryEnumUtils {
    private static final Log log = LogFactory.getLog(GlosseryEnumUtils.class);
    private static Map<String, Class> classes = new HashMap();
    private Map<String, String> enumTypes;

    public GlosseryEnumUtils() {
    }

    public static void setClasses(String shortName, String className) {
        Class name = null;

        try {
            name = Class.forName(className);
            if(!IGlossary.class.isAssignableFrom(name)) {
                throw new ApplicationException(name.getName() + "must be extend or implements  from IGlossary");
            }

            if(classes.containsKey(shortName)) {
                return;
            }

            if(classes.containsValue(name)) {
                return;
            }
        } catch (ClassNotFoundException var4) {
            throw new ApplicationException("init Spring Bean EnumUtil error", var4);
        }

        classes.put(shortName, name);
    }

    public Map<String, String> getEnumTypes() {
        return this.enumTypes;
    }

    public void setEnumTypes(Map<String, String> enumTypes) {
        this.enumTypes = enumTypes;
    }

    public void init() {
        Iterator items = this.enumTypes.keySet().iterator();

        while(items.hasNext()) {
            String name = (String)items.next();
            setClasses(name, (String)this.enumTypes.get(name));
        }

    }

    public void AddItem(String name, Class<? extends IGlossary> classz) {
        classes.put(name, classz);
    }

    public static <T extends IGlossary> List<T> getItems(Class<T> classz) {
        if(classz == null) {
            throw new ApplicationException("class can not be null");
        } else if(!classz.isEnum()) {
            throw new ApplicationException(classz.getName() + "must be Enum type.");
        } else if(!IGlossary.class.isAssignableFrom(classz)) {
            throw new ApplicationException(classz.getName() + "must be extend or implements  from IGlossary");
        } else {
            IGlossary[] enumConstants = (IGlossary[])classz.getEnumConstants();
            ArrayList list = new ArrayList();
            Collections.addAll(list, enumConstants);
            return list;
        }
    }

    public static <T extends IGlossary> List<T> getItemsByShortName(String shortName) {
        Class classz = (Class)classes.get(shortName);
        if(classz == null) {
            throw new ApplicationException("缺少枚举配置,类型  +" + shortName + "");
        } else {
            return getItems(classz);
        }
    }


    public static IGlossary getEnumName(String shortName, Integer value) {
        if(StringUtils.isBlank(shortName)){
            return null;
        }
        if(value == null) {
            return null;
        } else {
            List<IGlossary> items = getItems((Class)classes.get(shortName));
            for(IGlossary t:items){
                if(t.getValue().intValue()==value){
                    return t;
                }

            }
            return null;
        }
    }

    public static <T extends IGlossary> T getItem(Class<T> classz, Integer value) {
        List<T> items = getItems(classz);
        for(T t:items){
            if(t.getValue().intValue()==value){
                return t;
            }

        }
        return null;
    }
    public static <T extends IGlossary> T getItem(Class<T> classz, String name) {
        List<T> items = getItems(classz);
        for(T t:items){
            if(t instanceof Enum){
                Enum  e= (Enum) t;
                if(e.name().equals(name)){
                    return t;
                }
            }
        }
        return null;
    }

    public static  IGlossary getItem(String className, Integer value) {
        List<IGlossary> items = getItemsByShortName(className);
        for(IGlossary t:items){
            if(t.getValue().intValue()==value){
                return t;
            }

        }
        return null;
    }
}
