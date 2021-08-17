package com.chippy.redis.enhance.service.redistemplate.configuration;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceMethodInterceptor;
import com.chippy.redis.enhance.service.redistemplate.RedisTemplateEnhanceGetMethodInterceptor;
import com.chippy.redis.enhance.service.redistemplate.RedisTemplateEnhanceIncreaseMethodInterceptor;
import com.chippy.redis.enhance.service.redistemplate.RedisTemplateEnhanceObjectService;
import com.chippy.redis.enhance.service.redistemplate.RedisTemplateEnhanceSetMethodInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 基于${@link org.springframework.data.redis.core.RedisTemplate}实现的增强配置
 *
 * @author: chippy
 **/
@ConditionalOnProperty(prefix = "enhance-object", name = "client", havingValue = "redisTemplate")
@Configuration
public class RedisTemplateEnhanceObjectConfiguration {

    @Bean
    public RedisTemplateEnhanceGetMethodInterceptor redisTemplateEnhanceGetMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, StringRedisTemplate stringRedisTemplate,
        RedissonClient redissonClient) {
        return new RedisTemplateEnhanceGetMethodInterceptor(enhanceObjectManager, stringRedisTemplate, redissonClient);
    }

    @Bean
    public RedisTemplateEnhanceSetMethodInterceptor redisTemplateEnhanceSetMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, StringRedisTemplate stringRedisTemplate,
        RedissonClient redissonClient) {
        return new RedisTemplateEnhanceSetMethodInterceptor(enhanceObjectManager, stringRedisTemplate, redissonClient);
    }

    @Bean
    public RedisTemplateEnhanceIncreaseMethodInterceptor redisTemplateEnhanceIncreaseMethodInterceptor(
        EnhanceObjectManager enhanceObjectManager, StringRedisTemplate stringRedisTemplate,
        RedissonClient redissonClient) {
        return new RedisTemplateEnhanceIncreaseMethodInterceptor(enhanceObjectManager, stringRedisTemplate,
            redissonClient);
    }

    @Bean
    public RedisTemplateEnhanceObjectService redisTemplateEnhanceObjectService(
        List<EnhanceMethodInterceptor> enhanceMethodInterceptorList, StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateEnhanceObjectService(enhanceMethodInterceptorList, stringRedisTemplate);
    }

}