//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanCoper extends PropertyUtils {
    public BeanCoper() {
    }

    public static void copyProperties(Object desc, Object source) {
        try {
            PropertyUtils.copyProperties(desc, source);
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        } catch (InvocationTargetException var4) {
            var4.printStackTrace();
        } catch (NoSuchMethodException var5) {
            var5.printStackTrace();
        }

    }
}
