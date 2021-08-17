package com.chippy.redis.configuration;

import com.chippy.redis.enhance.EnhanceObjectLockCollector;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.EnhanceObjectProperties;
import com.chippy.redis.enhance.service.redisson.configuration.RedissonEnhanceObjectConfiguration;
import com.chippy.redis.enhance.service.redistemplate.configuration.RedisTemplateEnhanceObjectConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 增强对象自动配置类
 *
 * @author: chippy
 **/
@EnableConfigurationProperties(EnhanceObjectProperties.class)
@Configuration
@Import({RedissonEnhanceObjectConfiguration.class, RedisTemplateEnhanceObjectConfiguration.class})
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

    @ConditionalOnMissingBean
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

}