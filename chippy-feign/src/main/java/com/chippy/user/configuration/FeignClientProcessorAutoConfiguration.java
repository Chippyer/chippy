package com.chippy.user.configuration;

import com.chippy.user.support.processor.FeignClientProcessorHandler;
import com.chippy.user.support.registry.DefaultFeignClientProcessorRegistry;
import com.chippy.user.support.registry.ProcessorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * FeignClient请求扩展处理器自动配置
 *
 * @author: chippy
 * @datetime 2021/7/23 23:01
 */
@Configuration
public class FeignClientProcessorAutoConfiguration {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public ProcessorRegistry processorRegistry() {
        return new DefaultFeignClientProcessorRegistry(applicationContext);
    }

    @Bean
    public FeignClientProcessorHandler requestHandler() {
        return new FeignClientProcessorHandler();
    }

}
