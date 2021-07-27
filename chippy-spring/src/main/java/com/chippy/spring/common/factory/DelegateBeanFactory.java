package com.chippy.spring.common.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.spring.common.exception.DelegateBeanFactoryException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Spring BeanFactory更贴近业务的bean工厂
 *
 * @author: chippy
 * @datetime 2021/7/5 22:59
 */
@Service
public class DelegateBeanFactory {

    private static final String ERROR_LOG = "没有找到对应的服务-[class:%s, type: %s]";
    private static final long DEFAULT_LOCK_TIME = 1000L;
    private ReentrantLock refreshBeanCacheLock = new ReentrantLock();
    private final Map<Class<?>, Map<String, ? extends Type>> COPY_CONTEXT_BEAN_CACHE = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <T extends Type> T getBean(String type, Class<T> clazz) {
        final Map<String, ? extends Type> beanMap = this.getBeanMap(clazz);
        return (T)this.doGetBean(type, beanMap);
    }

    public <T extends Type> T getBean(String type, Class<T> clazz, boolean isValidateNull) {
        final T bean = this.getBean(type, clazz);
        if (isValidateNull && Objects.isNull(bean)) {
            throw new DelegateBeanFactoryException(String.format(ERROR_LOG, clazz.getName(), type));
        }
        return bean;
    }

    private void refreshCopyContextBeanCache(Class<? extends Type> clazz, Map<String, ? extends Type> beansOfType) {
        COPY_CONTEXT_BEAN_CACHE.put(clazz, beansOfType);
    }

    private Map<String, ? extends Type> getBeanMap(Class<? extends Type> clazz) {
        final Map<String, ? extends Type> copyBeansOfType = COPY_CONTEXT_BEAN_CACHE.get(clazz);
        if (ObjectsUtil.isNotEmpty(copyBeansOfType)) {
            return copyBeansOfType;
        }
        try {
            final boolean isLockHolder = refreshBeanCacheLock.tryLock(DEFAULT_LOCK_TIME, TimeUnit.MILLISECONDS);
            if (!isLockHolder) {
                throw new DelegateBeanFactoryException("实例操作繁忙，推荐预先初始化对应Bean信息");
            }
            final Map<String, ? extends Type> doubleCheckCopyBeansOfType = COPY_CONTEXT_BEAN_CACHE.get(clazz);
            if (ObjectsUtil.isNotEmpty(doubleCheckCopyBeansOfType)) {
                return doubleCheckCopyBeansOfType;
            }
            final Map<String, ? extends Type> beansOfType = applicationContext.getBeansOfType(clazz);
            if (CollectionUtil.isNotEmpty(beansOfType)) {
                this.refreshCopyContextBeanCache(clazz, beansOfType);
            }
            return beansOfType;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyMap();
        } finally {
            if (refreshBeanCacheLock.isHeldByCurrentThread()) {
                refreshBeanCacheLock.unlock();
            }
        }
    }

    private Type doGetBean(String type, Map<String, ? extends Type> beansOfType) {
        final Set<String> keySet = beansOfType.keySet();
        for (String key : keySet) {
            final Type bean = beansOfType.get(key);
            if (bean.getSupportTypeList().contains(type)) {
                return bean;
            }
        }
        return null;
    }

}
