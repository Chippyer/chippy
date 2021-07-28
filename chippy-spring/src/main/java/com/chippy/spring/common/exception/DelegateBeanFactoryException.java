package com.chippy.spring.common.exception;

/**
 * 基于Spring BeanFactory实现的托管业务工厂异常
 *
 * @author: chippy
 */
public class DelegateBeanFactoryException extends RuntimeException {

    public DelegateBeanFactoryException(String message) {
        super(message);
    }

}
