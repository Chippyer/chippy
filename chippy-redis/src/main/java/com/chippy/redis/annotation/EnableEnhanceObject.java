package com.chippy.redis.annotation;

import com.chippy.redis.configuration.EnhanceObjectAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动增强对象注解
 *
 * @author: chippy
 **/
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Import(EnhanceObjectAutoConfiguration.class)
public @interface EnableEnhanceObject {
}