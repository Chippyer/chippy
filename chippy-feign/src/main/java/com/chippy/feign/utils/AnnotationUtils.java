package com.chippy.feign.utils;

import com.chippy.common.utils.ObjectsUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationConfigurationException;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 基于Spring容器的注解工具类
 *
 * @author: chippy
 */
public class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils {

    public static <T extends Annotation> T getFirstAnnotation(ApplicationContext applicationContext, Class<T> clazz) {
        final Map beansWithAnnotation = applicationContext.getBeansWithAnnotation(clazz);
        if (ObjectsUtil.isEmpty(beansWithAnnotation)) {
            throw new AnnotationConfigurationException("请显示使用[" + clazz.getName() + "]注解");
        }
        Iterator iterator = beansWithAnnotation.keySet().iterator();
        final T annotation = findAnnotation(beansWithAnnotation.get(iterator.next()).getClass(), clazz);
        if (Objects.isNull(annotation)) {
            throw new AnnotationConfigurationException("请显示使用[" + clazz.getName() + "]注解");
        }
        return annotation;
    }

}
