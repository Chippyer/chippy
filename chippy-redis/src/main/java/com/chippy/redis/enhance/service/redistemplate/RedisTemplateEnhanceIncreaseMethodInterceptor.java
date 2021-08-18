package com.chippy.redis.enhance.service.redistemplate;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chippy.redis.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceIncreaseMethodInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link org.springframework.data.redis.core.RedisTemplate}实现的INCREASE方法增强
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceIncreaseMethodInterceptor extends EnhanceIncreaseMethodInterceptor {

    private StringRedisTemplate stringRedisTemplate;
    private RedissonClient redissonClient;

    public RedisTemplateEnhanceIncreaseMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        super(enhanceObjectManager);
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public Object increaseField(String id, String fieldName, Object arg) {
        if (ObjectUtil.isEmpty(arg)) {
            return null;
        }
        final String argStr = String.valueOf(arg);
        if (NumberUtil.isDouble(argStr)) {
            final Double valueOf = Double.valueOf(argStr);
            final double doubleValue = ObjectUtil.isEmpty(valueOf) ? NumberConstant.ZERO_DOUBLE : valueOf;
            return stringRedisTemplate.opsForHash().increment(id, fieldName, doubleValue);
        } else {
            final Long valueOf = Long.valueOf(argStr);
            final long longValue = ObjectUtil.isEmpty(valueOf) ? NumberConstant.ZERO_LONG : valueOf;
            return stringRedisTemplate.opsForHash().increment(id, fieldName, longValue);
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}