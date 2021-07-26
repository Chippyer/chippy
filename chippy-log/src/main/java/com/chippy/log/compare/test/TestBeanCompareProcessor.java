package com.chippy.log.compare.test;

import com.chippy.log.compare.support.GenericCompareProcessor;

/**
 * @title: 测试实现比较器
 * @author: chippy
 * @date: 2021-07-24 17:07
 **/
public class TestBeanCompareProcessor extends GenericCompareProcessor<OriginalBeanCompareData, OriginalBeanOperateLog> {

    @Override
    protected Class<OriginalBeanCompareData> getClassInstance() {
        return OriginalBeanCompareData.class;
    }

}