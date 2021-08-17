package com.chippy.redis.enhance.service.redisson;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceGetMethodInterceptor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link RedissonClient}实现的GET方法增强
 *
 * @author: chippy
 **/
public class RedissonEnhanceGetMethodInterceptor extends EnhanceGetMethodInterceptor {

    private RedissonClient redissonClient;

    public RedissonEnhanceGetMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.redissonClient = redissonClient;
    }

    @Override
    public Object getField(String id, String fieldName) {
        return redissonClient.getMap(id).get(fieldName);
    }

    @Override
    public Map<String, String> getFields(String id) {
        final RMap<Object, Object> entries = redissonClient.getMap(id);
        final Map<String, String> fieldMap = new HashMap<>(entries.size());
        entries.forEach((fieldName, fieldValue) -> fieldMap.put(String.valueOf(fieldName), String.valueOf(fieldValue)));
        return fieldMap;
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}