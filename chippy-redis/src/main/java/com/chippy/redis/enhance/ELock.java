package com.chippy.redis.enhance;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 增强对象锁
 *
 * @author: chippy
 **/
public interface ELock {

    /**
     * 获取增强对象读写锁
     *
     * @param lockKey 锁的key值
     * @return 读写锁
     * @author chippy
     */
    default ReadWriteLock getReadWriteLock(String lockKey) {
        return new ReentrantReadWriteLock();
    }

}