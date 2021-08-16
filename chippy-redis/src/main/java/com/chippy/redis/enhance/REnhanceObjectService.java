package com.chippy.redis.enhance;

import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

/**
 * 基于{@link RedissonClient}实现的增强服务BEAN构建工厂
 *
 * @author: chippy
 **/
public class REnhanceObjectService implements FactoryBean<RedissonEnhanceObjectService> {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public RedissonEnhanceObjectService getObject() {
        try {
            final EnhanceObjectManager enhanceObjectManager = applicationContext.getBean(EnhanceObjectManager.class);
            final RedissonClient redissonClient = applicationContext.getBean(RedissonClient.class);
            return new RedissonEnhanceObjectService(enhanceObjectManager, redissonClient);
        } catch (BeansException e) {
            throw new BeanCreationException(String.format("构建[%s] Bean异常", this.getObjectType()));
        }
    }

    @Override
    public Class<?> getObjectType() {
        return RedissonEnhanceObjectService.class;
    }

}