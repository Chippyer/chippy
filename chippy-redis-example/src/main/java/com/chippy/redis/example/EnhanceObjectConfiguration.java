package com.chippy.redis.example;

import com.chippy.redis.enhance.REnhanceObjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增强对象配置
 *
 * @author: chippy
 **/
@Configuration
public class EnhanceObjectConfiguration {

    @Bean
    public REnhanceObjectService rEnhanceObjectService() {
        return new REnhanceObjectService();
    }

}