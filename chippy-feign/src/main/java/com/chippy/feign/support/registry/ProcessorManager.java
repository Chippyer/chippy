package com.chippy.feign.support.registry;

import cn.hutool.core.collection.CollectionUtil;
import com.chippy.common.utils.CollectionsUtils;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.feign.support.processor.FeignClientProcessor;
import com.chippy.feign.utils.FeignClientProcessorComparator;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @title: 请求处理器管理器
 * @author: chippy
 * @date: 2021-07-23 15:07
 **/
@Slf4j
public class ProcessorManager {

    private static Map<String, List<FeignClientProcessor>> feignClientProcessorMap = new HashMap<>();

    public List<FeignClientProcessor> get(String fullPath) {
        final List<FeignClientProcessor> feignClientProcessorList = feignClientProcessorMap.get(fullPath);
        return CollectionUtil.isEmpty(feignClientProcessorList) ? Collections.emptyList() : feignClientProcessorList;
    }

    void register(String fullPath, FeignClientProcessor feignClientProcessor) {
        if (null == feignClientProcessor) {
            log.debug("注册FeignClientProcessor不能为空"); // 此处不应进入
            return;
        }
        final List<FeignClientProcessor> feignClientProcessors = feignClientProcessorMap.get(fullPath);
        if (ObjectsUtil.isEmpty(feignClientProcessors)) {
            final List<FeignClientProcessor> newFeignClientProcessor = new LinkedList<>();
            newFeignClientProcessor.add(feignClientProcessor);
            feignClientProcessorMap.put(fullPath, newFeignClientProcessor);
            return;
        }

        feignClientProcessors.add(feignClientProcessor);
        feignClientProcessorMap.put(fullPath, feignClientProcessors);
        feignClientProcessorMap.forEach((k, fcps) -> fcps.sort(new FeignClientProcessorComparator()));
    }

    public Object[] invokeProcessorBefore(RequestElement element, Object[] params,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object[] wrapParam = feignClientProcessor.processBefore(element, params);
            return invokeProcessorBefore(element, wrapParam, feignClientProcessorList, newSize);
        }
        return params;
    }

    public Object invokeProcessAfter(RequestElement element, Object response,
        List<FeignClientProcessor> feignClientProcessorList, int size) {
        if (size > 0) {
            int newSize = size - 1;
            final FeignClientProcessor feignClientProcessor = feignClientProcessorList.get(newSize);
            final Object wrapResponse = feignClientProcessor.processAfter(element, response);
            return invokeProcessAfter(element, wrapResponse, feignClientProcessorList, newSize);
        }
        return response;
    }

    public void processException(RequestElement element, List<FeignClientProcessor> feignClientProcessorList,
        Throwable e) {
        if (CollectionsUtils.isNotEmpty(feignClientProcessorList)) {
            for (FeignClientProcessor feignClientProcessor : feignClientProcessorList) {
                feignClientProcessor.processException(element, e);
            }
        }
    }

    private static volatile ProcessorManager processorManager;

    public static ProcessorManager getInstance() {
        if (ObjectsUtil.isNotEmpty(processorManager)) {
            return processorManager;
        }
        synchronized (ProcessorManager.class) {
            if (ObjectsUtil.isNotEmpty(processorManager)) {
                return processorManager;
            }
            processorManager = new ProcessorManager();
            return processorManager;
        }
    }

}