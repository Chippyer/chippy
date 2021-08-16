package com.chippy.redis.exception;

/**
 * 未执增强对象操作异常
 *
 * @author: chippy
 **/
public class UnknownEnhanceObjectException extends RuntimeException {

    public UnknownEnhanceObjectException(String message) {
        super(message);
    }

    public UnknownEnhanceObjectException(String message, Throwable cause) {
        super(message, cause);
    }

}