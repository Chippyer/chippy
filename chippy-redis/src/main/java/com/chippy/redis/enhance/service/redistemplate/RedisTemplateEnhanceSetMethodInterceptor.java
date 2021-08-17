package com.chippy.redis.enhance.service.redistemplate;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceSetMethodInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link org.springframework.data.redis.core.RedisTemplate}实现的SET方法增强
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceSetMethodInterceptor extends EnhanceSetMethodInterceptor {

    private StringRedisTemplate stringRedisTemplate;
    private RedissonClient redissonClient;

    public RedisTemplateEnhanceSetMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public void setField(String id, String fieldName, String fieldValue) {
        if (ObjectsUtil.isNotEmpty(fieldValue)) {
            stringRedisTemplate.opsForHash().put(id, fieldName, fieldValue);
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}