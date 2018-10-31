package com.common.util;

import com.common.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

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
        } catch (Exception var5) {
            throw new ApplicationException("copyProperties.error");
        }

    }

    public static <T> T copyProperties(Class<T> descType, Object source) {
        if (descType == null) {
            throw new ApplicationException("descType.error");
        }
        T entity = null;
        try {
            entity = (T) descType.getConstructors()[0].newInstance();
        } catch (Exception e) {
            throw new ApplicationException(descType.getSimpleName() + "Constructors.error");
        }
        copyProperties(entity, source);
        return entity;

    }
}
