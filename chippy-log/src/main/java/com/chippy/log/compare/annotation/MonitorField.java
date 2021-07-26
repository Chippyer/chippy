package com.chippy.log.compare.annotation;

import com.chippy.log.compare.support.CompareData;
import com.chippy.log.compare.support.GenericCompareProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title: 操作日志监控注解
 * <p>
 * 标识该注解字段支持
 * {@link GenericCompareProcessor#compareAndGet(CompareData, CompareData)
 * 返回变更后的操作记录信息
 * <p>
 * 标识该注解的字段不允许存在"null"值
 * @author: chippy
 * @date: 2021-05-25 21:05
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorField {

    /**
     * 操作描述
     */
    String operateDesc();

}