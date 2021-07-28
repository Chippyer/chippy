package com.chippy.log.compare.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志监控注解
 * 标识该注解的字段不允许存在"null"值
 *
 * @author: chippy
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorField {

    /**
     * 操作描述
     *
     * @return 操作描述
     */
    String operateDesc();

}