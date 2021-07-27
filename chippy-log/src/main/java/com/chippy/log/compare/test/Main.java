package com.chippy.log.compare.test;

import cn.hutool.json.JSONUtil;
import com.chippy.log.compare.support.GenericCompareData;
import com.chippy.log.compare.support.GenericCompareProcessor;

import java.util.List;

/**
 * @title: 测试主程序
 * @author: chippy
 * @date: 2021-07-24 17:07
 **/
public class Main {

    public static void main(String[] args) {
        final OriginalBean newTestBean1 = new OriginalBean();
        newTestBean1.setName("zhangsan");
        newTestBean1.setOperationId("1");
        newTestBean1.setOperationName("chippy");
        newTestBean1.setOperationType("UPDATE");
        final OriginalBean newTestBean2 = new OriginalBean();
        newTestBean2.setName("zhangsans");
        final GenericCompareProcessor testBeanCompareProcessor = new GenericCompareProcessor();
        final List<GenericCompareData> genericCompareData =
            testBeanCompareProcessor.compareAndGet(newTestBean1, newTestBean2);
        System.out.println(JSONUtil.toJsonStr(genericCompareData));
    }

}