package com.chippy.log.compare.test;

import cn.hutool.json.JSONUtil;

import java.util.List;

/**
 * @title: 测试主程序
 * @author: chippy
 * @date: 2021-07-24 17:07
 **/
public class Main {

    public static void main(String[] args) {
        final TestBean newTestBean1 = new TestBean();
        newTestBean1.setName("zhangsan");
        newTestBean1.setOperationId("1");
        newTestBean1.setOperationName("chippy");
        final TestBean newTestBean2 = new TestBean();
        newTestBean2.setName("zhangsans");
        final TestBeanCompareProcessor testBeanCompareProcessor = new TestBeanCompareProcessor();
        final List<TestBean> testBeans = testBeanCompareProcessor.compareAndGet(newTestBean1, newTestBean2);
        System.out.println(JSONUtil.toJsonStr(testBeans));
    }

}