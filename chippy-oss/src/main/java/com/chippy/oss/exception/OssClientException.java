package com.chippy.oss.exception;

/**
 * oss客户端异常
 *
 * @author: chippy
 */
public class OssClientException extends RuntimeException {

    public OssClientException(String message) {
        super(message);
    }

    public OssClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
