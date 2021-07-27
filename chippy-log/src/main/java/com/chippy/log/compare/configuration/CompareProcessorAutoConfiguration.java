package com.chippy.log.compare.configuration;

import com.chippy.log.compare.support.GenericCompareProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: 对象比较器自动配置类
 * @author: chippy
 * @date: 2021-07-27 14:07
 **/
@Configuration
public class CompareProcessorAutoConfiguration {

    @Bean
    public GenericCompareProcessor genericCompareProcessor() {
        return new GenericCompareProcessor();
    }

}