package com.chippy.log.compare.support;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @title: 规定操作日志比较时的基础属性
 * @author: chippy
 * @date: 2021-05-26 11:05
 **/
@Data
public class GenericCompareData {

    /**
     * 修改字段名称
     */
    private String itemName;

    /**
     * 原始属性值
     */
    private String oldItem;

    /**
     * 新属性值
     */
    private String newItem;

    /**
     * 操作类型（建议这个字段加上索引）
     */
    private String operationType;

    /**
     * 操作人唯一标识
     */
    private String operationId;

    /**
     * 操作人名称
     */
    private String operationName;

    /**
     * 创建时间
     */
    private Timestamp ctime;

}