package com.chippy.oss.exception;

/**
 * oss断言器异常
 *
 * @author: chippy
 **/
public class OssPredicateException extends RuntimeException {

    public OssPredicateException(String message) {
        super(message);
    }

    public OssPredicateException(String message, Throwable cause) {
        super(message, cause);
    }

}