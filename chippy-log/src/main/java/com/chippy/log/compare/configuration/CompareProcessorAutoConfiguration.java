package com.chippy.log.compare.configuration;

import com.chippy.log.compare.support.GenericCompareProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象比较器自动配置类
 *
 * @author: chippy
 **/
@Configuration
public class CompareProcessorAutoConfiguration {

    @Bean
    public GenericCompareProcessor genericCompareProcessor() {
        return new GenericCompareProcessor();
    }

}