package com.chippy.feign.exception;

/**
 * FeignClient调用结果异常
 *
 * @author: chippy
 **/
public class FeignClientRequestException extends RuntimeException {

    private int code = 200;

    public int getCode() {
        return code;
    }

    public FeignClientRequestException(String message) {
        super(message);
    }

    public FeignClientRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FeignClientRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

}