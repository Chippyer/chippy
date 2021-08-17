package com.chippy.redis.enhance.service;

import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 增强方法拦截器
 *
 * @author: chippy
 **/
public interface EnhanceMethodInterceptor {

    /**
     * 执行增强操作
     *
     * @param sourceObject 源对象
     * @param proxyObject  代理对象
     * @param method       源方法
     * @param proxyMethod  代理方法
     * @param args         参数
     * @return 方法返回值
     * @author chippy
     */
    Object process(Object sourceObject, Object proxyObject, Method method, MethodProxy proxyMethod, Object[] args)
        throws InvocationTargetException, IllegalAccessException;

    /**
     * 获取对应拦截器的支持方法
     *
     * @return 拦截器支持方法
     * @author chippy
     */
    String getSupportMethod();

    /**
     * 获取拦截器的支持方法参数个数
     *
     * @return 方法参数个数
     * @author chippy
     */
    int getArgsSize();

}