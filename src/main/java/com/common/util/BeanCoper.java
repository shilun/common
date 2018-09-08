//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanCoper extends PropertyUtils {
    public BeanCoper() {
    }

    public static void copyProperties(Object desc, Object source) {
        try {
            PropertyDescriptor[] propertyDescriptors = PropertyUtil.getPropertyDescriptors(desc);
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                if (descriptor.getName().startsWith("class")) {
                    continue;
                }
                PropertyDescriptor property = PropertyUtil.getPropertyDescriptor(source, descriptor.getName());
                if (property != null && property.getPropertyType() == descriptor.getPropertyType()) {
                    Object value = PropertyUtil.getProperty(source, descriptor.getName());
                    if (value != null)
                        PropertyUtil.setProperty(desc, descriptor.getName(), value);
                }
            }
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        } catch (InvocationTargetException var4) {
            var4.printStackTrace();
        } catch (NoSuchMethodException var5) {
            var5.printStackTrace();
        }

    }
}
