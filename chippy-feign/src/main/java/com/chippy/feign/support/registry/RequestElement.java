package com.chippy.feign.support.registry;

import lombok.Data;

/**
 * @title: 请求元素信息
 * @author: chippy
 * @date: 2021-07-23 16:07
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

}