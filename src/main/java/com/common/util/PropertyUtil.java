//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class PropertyUtil extends PropertyUtils {
    private static final Logger LOGGER = Logger.getLogger(PropertyUtil.class);

    public PropertyUtil() {
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            PropertyDescriptor[] e = PropertyUtils.getPropertyDescriptors(dest.getClass());
            PropertyDescriptor[] var3 = e;
            int var4 = e.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                PropertyDescriptor descriptor = var3[var5];
                if(!descriptor.getName().equalsIgnoreCase("class") && PropertyUtils.isReadable(orig, descriptor.getName())) {
                    Object property = PropertyUtils.getProperty(orig, descriptor.getName());
                    if(property != null && descriptor.getPropertyType() == property.getClass()) {
                        PropertyUtils.setProperty(dest, descriptor.getName(), property);
                    }
                }
            }

        } catch (Exception var8) {
            LOGGER.error("属性copy异常", var8);
            throw new RuntimeException(var8);
        }
    }
}
