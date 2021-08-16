package com.chippy.redis.example;

import com.chippy.redis.enhance.DefaultEnhanceObjectService;
import com.chippy.redis.enhance.EnhanceObjectManager;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link RedisTemplate}实现的增强对象操作实现
 * 支持分布式锁
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceObjectService extends DefaultEnhanceObjectService {

    private RedissonClient redissonClient;
    private RedisTemplate<String, String> redisTemplate;

    public RedisTemplateEnhanceObjectService(EnhanceObjectManager enhanceObjectManager, RedissonClient redissonClient,
        RedisTemplate<String, String> redisTemplate) {
        super(enhanceObjectManager);
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void setField(String id, String fieldName, String fieldValue) {
        redisTemplate.opsForHash().put(id, fieldName, fieldValue);
    }

    @Override
    protected Map<String, String> getField(String id) {
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(id);
        final Map<String, String> fieldMap = new HashMap<>(entries.size());
        entries.forEach((fieldName, fieldValue) -> fieldMap.put(String.valueOf(fieldName), String.valueOf(fieldValue)));
        return fieldMap;
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

    @Override
    protected Object increaseField(String id, String fieldName) {
        return redisTemplate.opsForHash().increment(id, fieldName, 1);
    }

}