package com.chippy.feign.utils;

import cn.hutool.core.bean.BeanException;

/**
 * Spring应用上下文工具类
 *
 * @author: chippy
 **/
public class ApplicationContextUtil {

    public static String getDefaultScannerPackage() {
        final StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                try {
                    final Class<?> mainClass = Class.forName(stackTraceElement.getClassName());
                    return mainClass.getPackage().getName();
                } catch (ClassNotFoundException e) {
                    throw new BeanException("未找到启动类-[{}]", e.getMessage(), e);
                }
            }
        }
        return null;
    }

}