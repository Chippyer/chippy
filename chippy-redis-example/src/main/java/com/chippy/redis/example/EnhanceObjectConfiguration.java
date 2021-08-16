package com.chippy.redis.example;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.EnhanceObjectService;
import com.chippy.redis.enhance.REnhanceObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

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

    @Bean
    public EnhanceObjectService redisTemplateEnhanceObjectService(EnhanceObjectManager enhanceObjectManager,
        RedissonClient redissonClient, RedisTemplate<String, String> redisTemplate) {
        return new RedisTemplateEnhanceObjectService(enhanceObjectManager, redissonClient, redisTemplate);
    }

}