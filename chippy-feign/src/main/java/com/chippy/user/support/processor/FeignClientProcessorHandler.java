package com.chippy.user.support.processor;

import com.chippy.user.support.registry.ProcessorManager;
import com.chippy.user.support.registry.RequestElement;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
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
public class FeignClientProcessorHandler {

    @Pointcut("@annotation(com.chippy.user.annotation.EnhanceRequest)")
    private void enhancePointCut() {
    }

    @Around("enhancePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        final String name = signature.getName();
        final String declaringTypeName = signature.getDeclaringTypeName();
        final Object[] args = joinPoint.getArgs();
        final RequestElement element = new RequestElement(declaringTypeName, name);

        final ProcessorManager processorManager = ProcessorManager.getInstance();
        final List<FeignClientProcessor> feignClientProcessorList = processorManager.get(element.getFullPath());
        final Object[] params =
            this.doInvokeProcessorBefore(element, args, feignClientProcessorList, feignClientProcessorList.size());
        try {
            final Object response = joinPoint.proceed(params);
            this.doInvokeProcessAfter(element, response, feignClientProcessorList, feignClientProcessorList.size());
            return response;
        } catch (Throwable throwable) {
            if (log.isErrorEnabled()) {
                log.error("请求调用未知异常-[{}]", throwable.getMessage(), throwable);
            }
            this.doProcessException(element, feignClientProcessorList, throwable);
            return null;
        }
    }

    private Object[] doInvokeProcessorBefore(RequestElement element, Object[] params,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object[] wrapParam = feignClientProcessor.processBefore(element, params);
            return doInvokeProcessorBefore(element, wrapParam, feignClientProcessorList, newSize);
        }
        return params;
    }

    private Object doInvokeProcessAfter(RequestElement element, Object response,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object wrapResponse = feignClientProcessor.processAfter(element, response);
            return doInvokeProcessAfter(element, wrapResponse, feignClientProcessorList, newSize);
        }
        return response;
    }

    private void doProcessException(RequestElement element, List<FeignClientProcessor> feignClientProcessorList,
        Throwable e) {
        if (null != feignClientProcessorList && !feignClientProcessorList.isEmpty()) {
            for (FeignClientProcessor feignClientProcessor : feignClientProcessorList) {
                feignClientProcessor.processException(element, e);
            }
        }
    }

}