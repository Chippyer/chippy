package com.chippy.redis.enhance.service.redisson;

import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceSetMethodInterceptor;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link RedissonClient}实现的SET方法增强
 *
 * @author: chippy
 **/
public class RedissonEnhanceSetMethodInterceptor extends EnhanceSetMethodInterceptor {

    private RedissonClient redissonClient;

    public RedissonEnhanceSetMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.redissonClient = redissonClient;
    }

    @Override
    public void setField(String id, String fieldName, String fieldValue) {
        redissonClient.getMap(id).put(fieldName, fieldValue);
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}