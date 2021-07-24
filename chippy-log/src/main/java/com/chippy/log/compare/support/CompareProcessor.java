package com.chippy.log.compare.support;

import java.util.List;

/**
 * @title: 对象对比处理器
 * @author: chippy
 * @date: 2021-07-24 17:07
 **/
public interface CompareProcessor<R extends GenericCompareData> {

    /**
     * 对比新老对象，并返回监控字段中被修改的值组成比较结果集合
     *
     * @param newCompareData 新对象
     * @param oldCompareData 老对象
     * @return java.util.List<com.chippy.log.compare.support.CompareData>
     * @author chippy
     * @date 2021-07-24 17:43
     */
    List<R> compareAndGet(GenericCompareData newCompareData, GenericCompareData oldCompareData);

}