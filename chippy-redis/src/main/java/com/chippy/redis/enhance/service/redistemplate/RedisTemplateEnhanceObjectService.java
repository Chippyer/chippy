package com.chippy.redis.enhance.service.redistemplate;

import com.chippy.redis.enhance.service.DefaultEnhanceObjectService;
import com.chippy.redis.enhance.service.EnhanceMethodInterceptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 基于{@link RedisTemplate}实现的增强对象操作实现
 * 支持分布式锁
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceObjectService extends DefaultEnhanceObjectService {

    private StringRedisTemplate stringRedisTemplate;

    public RedisTemplateEnhanceObjectService(List<EnhanceMethodInterceptor> enhanceMethodInterceptorList,
        StringRedisTemplate stringRedisTemplate) {
        super(enhanceMethodInterceptorList);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void shutdown(String id) {
        stringRedisTemplate.opsForHash().delete(id);
    }

}