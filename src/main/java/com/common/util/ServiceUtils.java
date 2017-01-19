package com.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ServiceUtils implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private Map<Class, Object> services = new HashMap();

    public Class getEntityType(Class classz) {
        ParameterizedType genericSuperclass = (ParameterizedType) classz.getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        return (Class) actualTypeArguments[0];
    }

    public DefaultBaseService getService(Class codeType) {
        return (DefaultBaseService) services.get(codeType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Class classz = DefaultBaseService.class;
        String[] types = applicationContext.getBeanNamesForType(classz);
        for (String type : types) {
            Object bean = applicationContext.getBean(type);
            services.put(getEntityType(bean.getClass()), bean);
        }
    }
}
