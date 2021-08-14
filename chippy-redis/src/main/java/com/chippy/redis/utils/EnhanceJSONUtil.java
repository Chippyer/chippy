package com.chippy.redis.utils;

import cn.hutool.json.JSONUtil;

import java.util.Objects;

/**
 * 增强JSONUtil
 *
 * @author: chippy
 **/
public class EnhanceJSONUtil {

    public static String toJsonStr(Object obj) {
        return isBaseType(obj) ? String.valueOf(obj) : JSONUtil.toJsonStr(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String jsonString, Class<T> beanClass) {
        Object result;
        if (Objects.equals(beanClass, String.class)) {
            result = jsonString;
        } else if (Objects.equals(beanClass, Long.class)) {
            result = Long.valueOf(jsonString);
        } else if (Objects.equals(beanClass, Double.class)) {
            result = Double.valueOf(jsonString);
        } else if (Objects.equals(beanClass, Float.class)) {
            result = Float.valueOf(jsonString);
        } else if (Objects.equals(beanClass, Integer.class)) {
            result = Integer.valueOf(jsonString);
        } else if (Objects.equals(beanClass, Short.class)) {
            result = Short.valueOf(jsonString);
        } else if (Objects.equals(beanClass, Character.class)) {
            result = jsonString.toCharArray()[0];
        } else if (Objects.equals(beanClass, Boolean.class)) {
            result = Boolean.valueOf(jsonString);
        }
        // not base type
        else {
            result = JSONUtil.toBean(jsonString, beanClass);
        }
        return (T)result;
    }

    private static boolean isBaseType(Object obj) {
        return obj instanceof String || obj instanceof Long || obj instanceof Double || obj instanceof Float
            || obj instanceof Integer || obj instanceof Short || obj instanceof Character || obj instanceof Boolean;
    }

}