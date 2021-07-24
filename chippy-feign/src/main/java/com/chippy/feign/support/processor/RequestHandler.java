package com.chippy.feign.support.processor;

import com.chippy.common.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @title: 请求处理器
 * @author: chippy
 * @date: 2021-07-24 11:07
 **/
public interface RequestHandler {

    /**
     * 执行FeignClient请求的核心方法
     * 用于指定FeignClient的真正调度启用
     *
     * @param joinPoint 切入点
     * @return com.chippy.common.result.Result
     * @author chippy
     * @date 2021-07-24 11:15
     */
    Result execute(ProceedingJoinPoint joinPoint);

}