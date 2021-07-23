package com.chippy.feign.utils;

import com.chippy.common.utils.ObjectsUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @title: Spring应用上下文工具类
 * @author: chippy
 * @date: 2021-07-23 16:07
 **/
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static String getDefaultScannerPackage() {
        final SpringBootApplication springBootApplication =
            AnnotationUtils.getFirstAnnotation(applicationContext, SpringBootApplication.class);
        if (ObjectsUtil.isEmpty(springBootApplication)) {
            return null;
        }
        return springBootApplication.getClass().getPackage().getName();
    }

}