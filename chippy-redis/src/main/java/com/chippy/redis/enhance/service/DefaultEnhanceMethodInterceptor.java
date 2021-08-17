package com.chippy.redis.enhance.service;

import com.chippy.redis.enhance.ELock;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.utils.EnhancerUtil;
import org.redisson.api.RLock;

import java.util.concurrent.locks.Lock;

/**
 * 默认增强方法拦截器实现
 *
 * @author: chippy
 **/
public abstract class DefaultEnhanceMethodInterceptor implements EnhanceMethodInterceptor, ELock {

    protected EnhanceObjectManager enhanceObjectManager;

    public DefaultEnhanceMethodInterceptor(EnhanceObjectManager enhanceObjectManager) {
        this.enhanceObjectManager = enhanceObjectManager;
    }

    protected String getFieldName(String methodName, String get) {
        return EnhancerUtil.lowerFirstCase(methodName.substring(get.length()));
    }

    protected String getLockKey(String fullClassName, String fieldName) {
        return fullClassName + fieldName;
    }

    protected void doUnLock(Lock writeLock) {
        if (writeLock instanceof RLock) {
            final RLock rWriteLock = (RLock)writeLock;
            if (rWriteLock.isHeldByCurrentThread()) {
                rWriteLock.unlock();
            }
        } else {
            writeLock.unlock();
        }
    }

}