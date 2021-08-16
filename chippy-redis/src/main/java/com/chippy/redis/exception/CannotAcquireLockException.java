package com.chippy.redis.exception;

/**
 * 无法获取锁异常
 *
 * @author: chippy
 **/
public class CannotAcquireLockException extends RuntimeException {

    public CannotAcquireLockException(String message) {
        super(message);
    }

    public CannotAcquireLockException(String message, Throwable cause) {
        super(message, cause);
    }

}