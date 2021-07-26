package com.chippy.log.compare.test;

import com.chippy.log.compare.support.GenericCompareData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title: 原始Bean
 * @author: chippy
 * @date: 2021-07-26 18:07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OriginalBeanOperateLog extends GenericCompareData {

    private String extra;

}