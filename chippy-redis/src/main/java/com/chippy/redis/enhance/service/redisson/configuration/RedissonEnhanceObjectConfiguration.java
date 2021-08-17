package com.chippy.redis.enhance.service.redisson.configuration;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceMethodInterceptor;
import com.chippy.redis.enhance.service.redisson.RedissonEnhanceGetMethodInterceptor;
import com.chippy.redis.enhance.service.redisson.RedissonEnhanceIncreaseMethodInterceptor;
import com.chippy.redis.enhance.service.redisson.RedissonEnhanceObjectService;
import com.chippy.redis.enhance.service.redisson.RedissonEnhanceSetMethodInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基于${@link RedissonClient}实现的增强配置
 *
 * @author: chippy
 **/
@ConditionalOnProperty(prefix = "enhance-object", name = "client", havingValue = "redisson")
@Configuration
public class RedissonEnhanceObjectConfiguration {

    @Bean
    public RedissonEnhanceGetMethodInterceptor redissonEnhanceGetMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, RedissonClient redissonClient) {
        return new RedissonEnhanceGetMethodInterceptor(enhanceObjectManager, redissonClient);
    }

    @Bean
    public RedissonEnhanceSetMethodInterceptor redissonEnhanceSetMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, RedissonClient redissonClient) {
        return new RedissonEnhanceSetMethodInterceptor(enhanceObjectManager, redissonClient);
    }

    @Bean
    public RedissonEnhanceIncreaseMethodInterceptor redissonEnhanceIncreaseMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, RedissonClient redissonClient) {
        return new RedissonEnhanceIncreaseMethodInterceptor(enhanceObjectManager, redissonClient);
    }

    @Bean
    public RedissonEnhanceObjectService redissonEnhanceObjectService(
        List<EnhanceMethodInterceptor> enhanceMethodInterceptorList, RedissonClient redissonClient) {
        return new RedissonEnhanceObjectService(enhanceMethodInterceptorList, redissonClient);
    }

}