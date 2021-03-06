package com.chippy.feign.support.registry;

import com.chippy.feign.support.processor.FeignClientProcessor;

/**
 * FeignProcessor处理器注册接口规范定义
 *
 * @author: chippy
 **/
public interface ProcessorRegistry {

    /**
     * 注册一个请求信息对应的处理器信息
     *
     * @param fullPath             请求全路径标识
     * @param feignClientProcessor FeignClient请求处理器
     * @author chippy
     */
    void register(String fullPath, FeignClientProcessor feignClientProcessor);

}