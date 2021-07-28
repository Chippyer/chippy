package com.chippy.log.compare.support;

import java.util.List;

/**
 * 对象对比处理器
 *
 * @author: chippy
 **/
public interface CompareProcessor<C, R> {

    /**
     * 对比新老对象，并返回监控字段中被修改的值组成比较结果集合
     *
     * @param newCompareData 新对象
     * @param oldCompareData 老对象
     * @return 返回对比后不同的比较操作信息
     * @author chippy
     */
    List<R> compareAndGet(C newCompareData, C oldCompareData);

}