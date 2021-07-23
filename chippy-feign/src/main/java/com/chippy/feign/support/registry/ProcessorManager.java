package com.chippy.feign.support.registry;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.feign.support.FeignClientProcessorComparator;
import com.chippy.feign.support.processor.FeignClientProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @title: 请求处理器管理器
 * @author: chippy
 * @date: 2021-07-23 15:07
 **/
@Slf4j
public class ProcessorManager {

    private static Map<String, List<FeignClientProcessor>> feignClientProcessorMap = new HashMap<>();

    public void register(String fullPath, List<FeignClientProcessor> feignClientProcessors) {
        if (null == feignClientProcessors || feignClientProcessors.isEmpty()) {
            log.debug("注册FeignClientProcessor不能为空"); // 此处不应进入
            return;
        }
        for (FeignClientProcessor feignClientProcessor : feignClientProcessors) {
            register(fullPath, feignClientProcessor);
        }
    }

    public void register(String fullPath, FeignClientProcessor feignClientProcessor) {
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