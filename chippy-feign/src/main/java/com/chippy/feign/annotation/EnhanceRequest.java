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

    /**
     * 遇到异常结果或异常信息是否抛出异常，默认抛出
     *
     * @return boolean
     * @author chippy
     * @date 2021-07-24 11:26
     */
    boolean isThrowEx() default true;

}