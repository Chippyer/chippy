package com.chippy.redis.enhance.service.redisson;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chippy.common.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.service.EnhanceIncreaseMethodInterceptor;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 基于{@link RedissonClient}实现的INCREASE方法增强
 *
 * @author: chippy
 **/
public class RedissonEnhanceIncreaseMethodInterceptor extends EnhanceIncreaseMethodInterceptor {

    private RedissonClient redissonClient;

    public RedissonEnhanceIncreaseMethodInterceptor(EnhanceObjectManager enhanceObjectManager,
        RedissonClient redissonClient) {
        super(enhanceObjectManager);
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
            return redissonClient.getMap(id).addAndGet(id, doubleValue);
        } else {
            final Long valueOf = Long.valueOf(argStr);
            final long longValue = ObjectUtil.isEmpty(valueOf) ? NumberConstant.ZERO_LONG : valueOf;
            return redissonClient.getMap(id).addAndGet(id, longValue);
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

}