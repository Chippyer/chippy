package com.chippy.redis.utils;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;

/**
 * cglib工具类
 *
 * @author: chippy
 **/
public class EnhancerUtil {

    public static final String SET = "set";
    public static final String GET = "get";
    public static final String INCREASE = "increase";

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T object, Callback callback) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(callback);
        return (T)enhancer.create();
    }

    public static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

}