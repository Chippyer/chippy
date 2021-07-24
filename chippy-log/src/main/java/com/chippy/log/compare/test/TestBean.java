package com.chippy.log.compare.test;

import com.chippy.log.compare.annotation.MonitorField;
import com.chippy.log.compare.support.GenericCompareData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title: 测试Bean
 * @author: chippy
 * @date: 2021-07-24 17:07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TestBean extends GenericCompareData {

    private String id;

    @MonitorField(operateDesc = "修改名称操作")
    private String name;

}