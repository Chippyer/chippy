package com.chippy.redis.exception;

/**
 * 增强对象拦截器异常
 *
 * @author: chippy
 **/
public class EnhanceMethodInterceptorException extends RuntimeException {

    public EnhanceMethodInterceptorException(String message) {
        super(message);
    }

    public EnhanceMethodInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

}