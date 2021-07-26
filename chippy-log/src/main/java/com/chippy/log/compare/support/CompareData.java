package com.chippy.log.compare.support;

/**
 * @title: 比较参数规范定义
 * @author: chippy
 * @date: 2021-07-26 18:07
 **/
public interface CompareData {

    /**
     * 操作人标识
     *
     * @return java.lang.String
     * @author chippy
     * @date 2021-07-26 18:26
     */
    String getOperationId();

    /**
     * 操作人名称
     *
     * @return java.lang.String
     * @author chippy
     * @date 2021-07-26 18:26
     */
    String getOperationName();

}