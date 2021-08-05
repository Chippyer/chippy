package com.chippy.oss.exception;

/**
 * oss 客户端异常
 *
 * @author: chippy
 */
public class ClientException extends RuntimeException {

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
