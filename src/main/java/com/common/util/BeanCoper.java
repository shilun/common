package com.common.util;

import com.common.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class BeanCoper extends PropertyUtils {
    public BeanCoper() {
    }

    /**
     * 对象copy
     * @param desc 目标对象
     * @param source 源对象
     */
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
                    if (value != null) PropertyUtil.setProperty(desc, descriptor.getName(), value);
                }
            }
        } catch (Exception var5) {
            throw new ApplicationException("copyProperties.error");
        }
    }

    /**
     * 对象copy
     * @param descType 目标类型
     * @param source 源对象
     * @param <T>
     * @return
     */
    public static <T> T clone(Class<T> descType, Object source) {
       return copyProperties(descType,source);
    }

    /**
     * copy对象
     * @param descType 目标类型
     * @param listSource 源类型
     * @param <T>
     * @return
     */
    public static <T> List<T> clone(Class<T> descType, List listSource) {
        List resultList = new ArrayList();
        listSource.forEach((e) -> {
            resultList.add(copyProperties(descType, e));
        });
        return resultList;
    }
    /**
     * 分页数据copy
     * @param descType 目标类型
     * @param sourcePage 源类型
     * @param <T>
     * @return
     */
    public static <T> Page<T> clone(Class<T> descType, Page sourcePage) {
        List<T> resultList=new ArrayList<>();
        sourcePage.getContent().forEach((e) -> {
            resultList.add(copyProperties(descType, e));
        });
        return new PageImpl(resultList,sourcePage.getPageable(),sourcePage.getTotalElements());
    }

    /**
     * List copy
     * @param descType 目标类型
     * @param listSource 源类型
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(Class<T> descType, List listSource) {
        List resultList = new ArrayList();
        listSource.forEach((e) -> {
            resultList.add(copyProperties(descType, e));
        });
        return resultList;
    }


    /**
     * 分页数据copy
     * @param descType 目标类型
     * @param sourcePage 源类型
     * @param <T>
     * @return
     */
    public static <T> Page<T> copyPage(Class<T> descType, Page sourcePage) {
        List<T> resultList=new ArrayList<>();
        sourcePage.getContent().forEach((e) -> {
            resultList.add(copyProperties(descType, e));
        });
        return new PageImpl(resultList,sourcePage.getPageable(),sourcePage.getTotalElements());
    }

    /**
     * 对象copy
     * @param descType 目标类型
     * @param source 源对象
     * @param <T>
     * @return
     */
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
