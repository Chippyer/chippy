package com.chippy.redis.configuration;

import com.chippy.redis.enhance.RedisTemplateEnhanceObjectService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 增强对象自动配置类
 *
 * @author: chippy
 **/
@Configuration
public class EnhanceObjectAutoConfiguration {

    @ConditionalOnBean(StringRedisTemplate.class)
    @Bean
    public RedisTemplateEnhanceObjectService redisTemplateEnhanceObjectService(StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateEnhanceObjectService(stringRedisTemplate);
    }

}