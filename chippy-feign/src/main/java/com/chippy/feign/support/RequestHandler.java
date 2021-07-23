package com.chippy.feign.support;

import com.chippy.feign.support.registry.RequestElement;
import com.chippy.feign.support.processor.FeignClientProcessor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * @title: 请求处理器
 * @author: chippy
 * @date: 2021-07-23 16:07
 **/
@Aspect
@Slf4j
public class RequestHandler {

    @Pointcut("@annotation(com.chippy.feign.annotation.EnhanceRequest)")
    private void enhancePointCut() {
    }

    @AfterReturning(value = "enhancePointCut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        /*
            todo
            1. 获取全路径信息 classPath + methodName
            2. 获取方法参数
            3. 获取对应元素信息
            4. 获取对应的处理器
            5. 如果处理器存在则在执行前后执行处理器对应操作
        */
    }

    private static Object[] doInvokeProcessorBefore(RequestElement element, Object[] params,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object[] wrapParam = feignClientProcessor.processBefore(element, params);
            return doInvokeProcessorBefore(element, wrapParam, feignClientProcessorList, newSize);
        }
        return params;
    }

    private static Object doInvokeProcessAfter(RequestElement element, Object response,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object wrapResponse = feignClientProcessor.processAfter(element, response);
            return doInvokeProcessAfter(element, wrapResponse, feignClientProcessorList, newSize);
        }
        return response;
    }

}