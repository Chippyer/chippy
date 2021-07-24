package com.chippy.feign.support.processor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @title: 请求处理器调度器
 * @author: chippy
 * @date: 2021-07-23 16:07
 **/
@Aspect
@Slf4j
public class FeignClientProcessorScheduler {

    private RequestHandler requestHandler;

    public FeignClientProcessorScheduler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Pointcut("@within(com.chippy.feign.annotation.EnhanceRequest) || @annotation(com.chippy.feign.annotation.EnhanceRequest)")
    private void enhancePointCut() {
    }

    @Around("enhancePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        return requestHandler.execute(joinPoint);
    }

}