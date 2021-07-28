package com.chippy.log.compare.support;

import lombok.Data;

/**
 * 规定操作日志比较时的基础属性
 *
 * @author: chippy
 **/
@Data
public class GenericCompareData implements CompareData {

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
     * 操作描述
     */
    private String operationDesc;

}