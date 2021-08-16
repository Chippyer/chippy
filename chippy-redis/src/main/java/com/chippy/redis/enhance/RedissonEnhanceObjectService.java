package com.chippy.redis.enhance;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link RedissonClient}实现的增强对象操作实现
 * 支持分布式锁
 *
 * @author: chippy
 **/
public class RedissonEnhanceObjectService extends DefaultEnhanceObjectService {

    private RedissonClient redissonClient;

    public RedissonEnhanceObjectService(EnhanceObjectManager enhanceObjectManager, RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.redissonClient = redissonClient;
    }

    @Override
    protected void doSetField(String id, String fieldName, String fieldValue) {
        redissonClient.getMap(id).put(fieldName, fieldValue);
    }

    @Override
    protected Map<String, String> doGetField(String id) {
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