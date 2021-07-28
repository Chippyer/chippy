package com.chippy.feign.support.registry;

import lombok.Data;

/**
 * 请求元素信息
 *
 * @author: chippy
 **/
@Data
public class RequestElement {

    /**
     * 请求类类名
     */
    private String className;

    /**
     * 请求类方法名
     */
    private String methodName;

    /**
     * 请求方法全路径标识 className + methodName
     */
    private String fullPath;

    public RequestElement() {
    }

    public RequestElement(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.fullPath = className + methodName;
    }

}