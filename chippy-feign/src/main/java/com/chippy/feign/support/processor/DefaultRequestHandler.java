package com.chippy.feign.support.processor;

import com.chippy.common.result.Result;
import com.chippy.feign.annotation.EnhanceRequest;
import com.chippy.feign.exception.FeignClientRequestException;
import com.chippy.feign.support.registry.ProcessorManager;
import com.chippy.feign.support.registry.RequestElement;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

/**
 * 默认请求处理器实现
 *
 * @author: chippy
 **/
@Slf4j
public class DefaultRequestHandler implements RequestHandler {

    private ProcessorManager processorManager;

    public DefaultRequestHandler() {
        this.processorManager = ProcessorManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result execute(ProceedingJoinPoint joinPoint) {
        final MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        final String methodName = methodSignature.getName();
        final String className = methodSignature.getDeclaringTypeName();
        final Object[] originalParam = joinPoint.getArgs();
        final RequestElement element = new RequestElement(className, methodName);

        final List<FeignClientProcessor> feignClientProcessorList = this.getFeignClientProcessorList(element);
        final Object[] wrapParams = processorManager
            .invokeProcessorBefore(element, originalParam, feignClientProcessorList, feignClientProcessorList.size());
        try {
            final Result response = (Result)joinPoint.proceed(wrapParams);
            processorManager
                .invokeProcessAfter(element, response, feignClientProcessorList, feignClientProcessorList.size());

            if (response.getCode() == response.definitionSuccessCode()) {
                return response;
            }
            if (this.isThrowEx(methodSignature)) {
                throw new FeignClientRequestException(response.getCode(), response.getErrorMsg());
            } else {
                if (log.isErrorEnabled()) {
                    log.error("请求调用结果异常");
                }
                response.setData(null);
                return response;
            }
        } catch (Throwable throwable) {
            processorManager.processException(element, feignClientProcessorList, throwable);
        }
        // 这里不应该被触发
        return null;
    }

    private List<FeignClientProcessor> getFeignClientProcessorList(RequestElement element) {
        return processorManager.get(element.getFullPath());
    }

    private boolean isThrowEx(MethodSignature signature) {
        final EnhanceRequest enhanceRequest = signature.getMethod().getAnnotation(EnhanceRequest.class);
        return enhanceRequest.isThrowEx();
    }

}