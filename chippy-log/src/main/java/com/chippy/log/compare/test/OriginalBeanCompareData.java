package com.chippy.log.compare.test;

import com.chippy.log.compare.support.CompareData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title: 比较器
 * @author: chippy
 * @date: 2021-07-26 18:07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OriginalBeanCompareData extends OriginalBean implements CompareData {

    /**
     * 操作类型
     */
    private String operationType;

}