package com.chippy.redis.configuration;

import com.chippy.redis.enhance.EnhanceObjectLockCollector;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.EnhanceObjectProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增强对象自动配置类
 *
 * @author: chippy
 **/
@EnableConfigurationProperties(EnhanceObjectProperties.class)
@Configuration
public class EnhanceObjectAutoConfiguration {

    @Bean
    public EnhanceObjectManager enhanceObjectManager(EnhanceObjectProperties enhanceObjectProperties) {
        return new EnhanceObjectManager(enhanceObjectProperties.getEnhanceObjectCapacity());
    }

    @Bean
    public EnhanceObjectLockCollector enhanceObjectLockCollector(EnhanceObjectManager enhanceObjectManager,
        EnhanceObjectProperties enhanceObjectProperties) {
        return new EnhanceObjectLockCollector(enhanceObjectProperties.getScannerPackage(), enhanceObjectManager);
    }

}