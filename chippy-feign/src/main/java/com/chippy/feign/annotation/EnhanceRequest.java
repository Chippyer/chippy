package com.chippy.feign.annotation;

import java.lang.annotation.*;

/**
 * @title: FeignClient请求前后处理标识
 * <p>
 * 调用前后触发器
 * @author: chippy
 * @date: 2021-07-23 11:07
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnhanceRequest {
}