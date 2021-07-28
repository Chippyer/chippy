package com.chippy.feign.configuration;

import com.chippy.feign.support.processor.DefaultRequestHandler;
import com.chippy.feign.support.processor.FeignClientProcessorScheduler;
import com.chippy.feign.support.processor.RequestHandler;
import com.chippy.feign.support.registry.DefaultFeignClientProcessorRegistry;
import com.chippy.feign.support.registry.ProcessorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * FeignClient请求扩展处理器自动配置
 *
 * @author: chippy
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
    @ConditionalOnMissingBean
    public RequestHandler requestHandler() {
        return new DefaultRequestHandler();
    }

    @Bean
    public FeignClientProcessorScheduler feignClientProcessorScheduler(RequestHandler requestHandler) {
        return new FeignClientProcessorScheduler(requestHandler);
    }

}
