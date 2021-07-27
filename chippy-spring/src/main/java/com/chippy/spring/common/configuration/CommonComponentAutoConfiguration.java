package com.chippy.spring.common.configuration;

import com.chippy.spring.common.factory.DelegateBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: 公共组件自动配置类
 * @author: chippy
 * @date: 2021-07-27 14:07
 **/
@Configuration
public class CommonComponentAutoConfiguration {

    @Bean
    public DelegateBeanFactory delegateBeanFactory() {
        return new DelegateBeanFactory();
    }

}