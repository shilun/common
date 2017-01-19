//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import com.common.util.IGlossary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    public static <T extends IGlossary> List<IGlossary> getItems(Class<T> classz) {
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

    public static <T extends IGlossary> List<IGlossary> getItemsByShortName(String shortName) {
        Class classz = (Class)classes.get(shortName);
        if(classz == null) {
            throw new ApplicationException("缺少枚举配置,类型  +" + shortName + "");
        } else {
            return getItems(classz);
        }
    }

    public static <T extends IGlossary> IGlossary getItemsByShortName(String shortName, Integer value) {
        if(value == null) {
            return null;
        } else {
            List items = getItems((Class)classes.get(shortName));
            Iterator var3 = items.iterator();

            IGlossary item;
            do {
                if(!var3.hasNext()) {
                    return null;
                }

                item = (IGlossary)var3.next();
            } while(item.getValue().intValue() != value.intValue());

            return item;
        }
    }

    public static <T extends IGlossary> IGlossary getEnumName(String shortName, Integer value) {
        if(value == null) {
            return null;
        } else {
            List items = getItems((Class)classes.get(shortName));
            Iterator var3 = items.iterator();

            IGlossary item;
            do {
                if(!var3.hasNext()) {
                    return null;
                }

                item = (IGlossary)var3.next();
            } while(item.getValue().intValue() != value.intValue());

            return item;
        }
    }

    public static <T extends IGlossary> IGlossary getItem(Class<T> classz, Integer value) {
        List items = getItems(classz);
        Iterator var3 = items.iterator();

        IGlossary item;
        do {
            if(!var3.hasNext()) {
                return null;
            }

            item = (IGlossary)var3.next();
        } while(item.getValue().intValue() != value.intValue());

        return item;
    }

    public static <T extends IGlossary> IGlossary getItem(String className, Integer value) {
        List items = getItemsByShortName(className);
        Iterator var3 = items.iterator();

        IGlossary item;
        do {
            if(!var3.hasNext()) {
                return null;
            }

            item = (IGlossary)var3.next();
        } while(item.getValue().intValue() != value.intValue());

        return item;
    }
}
