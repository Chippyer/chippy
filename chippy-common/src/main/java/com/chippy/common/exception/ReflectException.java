package com.chippy.common.exception;

/**
 * 反射操作异常
 *
 * @author: chippy
 */
public class ReflectException extends RuntimeException {

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

}
