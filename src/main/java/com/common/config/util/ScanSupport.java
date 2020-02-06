package com.common.config.util;

import com.common.exception.ApplicationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ScanSupport implements ResourceLoaderAware, ApplicationContextAware {
    /**
     * Spring容器注入
     */
    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resolver;
    private MetadataReaderFactory metadataReaderFactory;
    private String entityPackage;

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    /**
     * set注入对象
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 利用spring提供的扫描包下面的类信息,再通过classfrom反射获得类信息
     *
     * @param scanPath
     * @return
     * @throws IOException
     */
    public Set<Class<?>> doScan(String scanPath) throws IOException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPath)).concat("/**/*.class"));
        Resource[] resources = resolver.getResources(packageSearchPath);
        MetadataReader metadataReader = null;
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                metadataReader = metadataReaderFactory.getMetadataReader(resource);
                try {
                    if (metadataReader.getClassMetadata().isConcrete()) {// 当类型不是抽象类或接口在添加到集合
                        classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    /**
     * 指定包下面的类信息
     *
     * @return 多个类信息
     */
    public Set<Class<?>> classInfos() {
        try {
            return doScan(entityPackage);
        } catch (Exception e) {
            throw new ApplicationException("扫描包类信息错误", e);
        }
    }
}