package com.chippy.redis.enhance.service.redisson;

import com.chippy.redis.enhance.service.DefaultEnhanceObjectService;
import com.chippy.redis.enhance.service.EnhanceMethodInterceptor;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * 基于{@link RedissonClient}实现的增强对象操作实现
 * 支持分布式锁
 *
 * @author: chippy
 **/
public class RedissonEnhanceObjectService extends DefaultEnhanceObjectService {

    private RedissonClient redissonClient;

    public RedissonEnhanceObjectService(List<EnhanceMethodInterceptor> enhanceMethodInterceptorList,
        RedissonClient redissonClient) {
        super(enhanceMethodInterceptorList);
        this.redissonClient = redissonClient;
    }

    @Override
    public void shutdown(String id) {
        redissonClient.getMap(id).delete();
    }

}