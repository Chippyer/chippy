package com.chippy.redis.enhance;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.chippy.common.utils.ObjectsUtil;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强对象锁信息收集器
 *
 * @author: chippy
 **/
public class EnhanceObjectLockCollector implements InitializingBean {

    private List<String> packages;

    private EnhanceObjectManager enhanceObjectManager;

    public EnhanceObjectLockCollector(List<String> packages, EnhanceObjectManager enhanceObjectManager) {
        this.packages = packages;
        this.enhanceObjectManager = enhanceObjectManager;
    }

    public void collect() {
        this.validate();
        this.doCollect();
    }

    private void doCollect() {
        final Set<Class<?>> classSet =
            this.packages.stream().map(ClassUtil::scanPackage).flatMap(Collection::stream).collect(Collectors.toSet());
        classSet.forEach(this::resolveClass);
    }

    private void validate() {
        if (ObjectsUtil.isEmpty(packages)) {
            throw new BeanCreationException("[增强对象信息管理器]扫描路径为空");
        }
        if (ObjectsUtil.isEmpty(enhanceObjectManager)) {
            throw new BeanCreationException("[增强对象信息管理器]为空");
        }
    }

    private void resolveClass(Class<?> classType) {
        final Class<?>[] interfaces = classType.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            this.resolveEnhanceObjectClass(classType, interfaceClass);
        }
    }

    private void resolveEnhanceObjectClass(Class<?> classType, Class<?> interfaceClass) {
        if (Objects.equals(interfaceClass, EnhanceObject.class)) {
            final String fullClassName = classType.getName();
            final EnhanceObjectInfo enhanceObjectInfo = new EnhanceObjectInfo();
            enhanceObjectInfo.setClassName(classType.getSimpleName());
            enhanceObjectInfo.setClassType(classType);
            enhanceObjectInfo.setFullClassName(fullClassName);
            this.resolveEnhanceObjectField(classType, enhanceObjectInfo);
            enhanceObjectManager.register(fullClassName, enhanceObjectInfo);
        }
    }

    private void resolveEnhanceObjectField(Class<?> classType, EnhanceObjectInfo enhanceObjectInfo) {
        final Field[] fields = classType.getDeclaredFields();
        final List<EnhanceObjectField> enhanceObjectFieldList = new ArrayList<>(fields.length);
        final List<String> excludeFieldList = new ArrayList<>(fields.length);
        for (Field field : fields) {
            final FieldLock fieldLock = AnnotationUtil.getAnnotation(field, FieldLock.class);
            if (Objects.nonNull(fieldLock)) {
                final EnhanceObjectField enhanceObjectField = new EnhanceObjectField();
                enhanceObjectField.setField(field);
                enhanceObjectField.setIsLock(Boolean.TRUE);
                enhanceObjectField.setFieldLockType(fieldLock.fieldLockType());
                enhanceObjectField.setWaitLockTime(fieldLock.waitLockTime());
                enhanceObjectFieldList.add(enhanceObjectField);
            } else {
                excludeFieldList.add(field.getName());
            }
        }
        enhanceObjectInfo.setEnhanceObjectFieldList(enhanceObjectFieldList);
        enhanceObjectInfo.setExcludeFieldList(excludeFieldList);
    }

    @Override
    public void afterPropertiesSet() {
        this.collect();
    }

}