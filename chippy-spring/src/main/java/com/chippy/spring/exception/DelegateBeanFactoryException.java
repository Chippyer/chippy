package com.chippy.spring.exception;

/**
 * 基于Spring BeanFactory实现的托管业务工厂异常
 *
 * @author: chippy
 * @datetime 2021/7/5 23:02
 */
public class DelegateBeanFactoryException extends RuntimeException {

    public DelegateBeanFactoryException(String message) {
        super(message);
    }

}
