package com.chippy.redis.enhance.service;

import cn.hutool.core.util.ObjectUtil;
import com.chippy.common.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.enhance.EnhanceObjectField;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.FieldLockType;
import com.chippy.redis.exception.CannotAcquireLockException;
import com.chippy.redis.exception.UnknownEnhanceObjectException;
import com.chippy.redis.utils.EnhancerUtil;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 增强SET方法拦截器
 *
 * @author: chippy
 **/
public abstract class EnhanceSetMethodInterceptor extends DefaultEnhanceMethodInterceptor {

    public EnhanceSetMethodInterceptor(EnhanceObjectManager enhanceObjectManager) {
        super(enhanceObjectManager);
    }

    @Override
    public Object process(Object sourceObject, Object proxyObject, Method method, MethodProxy proxyMethod,
        Object[] args) throws InvocationTargetException, IllegalAccessException {
        final String methodName = method.getName();
        final String fullClassName = sourceObject.getClass().getName();
        final EnhanceObject enhanceObject = (EnhanceObject)sourceObject;

        final String fieldName = this.getFieldName(methodName, EnhancerUtil.SET);
        final List<String> excludeFieldList = enhanceObjectManager.getExcludeFieldList(fullClassName);
        if (Objects.isNull(excludeFieldList) || excludeFieldList.contains(fieldName)) {
            return this.normalInvokeSet(sourceObject, method, args, enhanceObject, fieldName);
        }

        final EnhanceObjectField enhanceObjectFiled =
            enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
        final boolean notEmpty = ObjectUtil.isNotEmpty(enhanceObjectFiled);
        final boolean isLock = enhanceObjectFiled.getIsLock();
        final boolean isSupportWriteLock = FieldLockType.supportW(enhanceObjectFiled.getFieldLockType());
        if (notEmpty && isLock && isSupportWriteLock) {
            return this.lockInvokeSet(sourceObject, method, enhanceObject, fullClassName, enhanceObjectFiled,
                args[NumberConstant.ZERO_INT]);
        } else {
            return this.normalInvokeSet(sourceObject, method, args, enhanceObject, fieldName);
        }
    }

    private <T> Object lockInvokeSet(T sourceObject, Method method, EnhanceObject enhanceObject, String fullClassName,
        EnhanceObjectField enhanceObjectFiled, Object arg) {
        final String fieldName = enhanceObjectFiled.getField().getName();
        Lock writeLock = null;
        try {
            writeLock = this.getReadWriteLock(this.getLockKey(fullClassName, fieldName)).writeLock();
            final boolean isWriteLockHolder =
                writeLock.tryLock(enhanceObjectFiled.getWaitLockTime(), TimeUnit.MILLISECONDS);
            if (!isWriteLockHolder) {
                throw new CannotAcquireLockException(String
                    .format("获取[class:%s, field:%s]写锁时间超时[timeout:%s]", fullClassName, fieldName,
                        enhanceObjectFiled.getWaitLockTime()));
            }
            this.setField(enhanceObject.getId(), fieldName, String.valueOf(arg));
            return method.invoke(sourceObject, arg);
        } catch (Exception e) {
            throw new UnknownEnhanceObjectException(e.getMessage(), e);
        } finally {
            if (Objects.nonNull(writeLock)) {
                this.doUnLock(writeLock);
            }
        }
    }

    private <T> Object normalInvokeSet(T sourceObject, Method method, Object[] args, EnhanceObject enhanceObject,
        String fieldName) throws IllegalAccessException, InvocationTargetException {
        this.setField(enhanceObject.getId(), fieldName, String.valueOf(args[0]));
        return method.invoke(sourceObject, args);
    }

    @Override
    public String getSupportMethod() {
        return EnhancerUtil.SET;
    }

    @Override
    public int getArgsSize() {
        return NumberConstant.ONE_INT;
    }

    public abstract void setField(String id, String fieldName, String fieldValue);

}