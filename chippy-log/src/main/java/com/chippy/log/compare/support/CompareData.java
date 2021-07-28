package com.chippy.log.compare.support;

/**
 * 比较参数规范定义
 *
 * @author: chippy
 **/
public interface CompareData {

    /**
     * 操作人标识
     *
     * @return java.lang.String
     * @author chippy
     */
    String getOperationId();

    /**
     * 操作人名称
     *
     * @return java.lang.String
     * @author chippy
     */
    String getOperationName();

}