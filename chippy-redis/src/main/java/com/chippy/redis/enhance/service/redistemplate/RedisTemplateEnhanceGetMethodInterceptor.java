package com.chippy.redis.enhance.service.redistemplate;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceGetMethodInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link org.springframework.data.redis.core.RedisTemplate}实现的GET方法增强
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceGetMethodInterceptor extends EnhanceGetMethodInterceptor {

    private StringRedisTemplate stringRedisTemplate;
    private RedissonClient redissonClient;

    public RedisTemplateEnhanceGetMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public Object getField(String id, String fieldName) {
        return stringRedisTemplate.opsForHash().get(id, fieldName);
    }

    @Override
    public Map<String, String> getFields(String id) {
        final Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(id);
        final Map<String, String> fieldMap = new HashMap<>(entries.size());
        entries.forEach((fieldName, fieldValue) -> fieldMap.put(String.valueOf(fieldName), String.valueOf(fieldValue)));
        return fieldMap;
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}