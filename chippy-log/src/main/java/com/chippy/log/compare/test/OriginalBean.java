package com.chippy.log.compare.test;

import com.chippy.log.compare.annotation.MonitorField;
import com.chippy.log.compare.support.CompareData;
import com.chippy.log.compare.support.OperationRelationCompareData;
import lombok.Data;

/**
 * @title: 原始Bean
 * @author: chippy
 * @date: 2021-07-26 18:07
 **/
@Data
public class OriginalBean implements CompareData, OperationRelationCompareData {

    private Long id;

    @MonitorField(operateDesc = "修改名称")
    private String name;

    /**
     * 操作人ID
     */
    private String operationId;

    /**
     * 操作人名称
     */
    private String operationName;

    /**
     * 操作类型
     */
    private String operationType;

}