package com.chippy.redis.enhance.service;

import cn.hutool.core.util.ObjectUtil;
import com.chippy.common.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.enhance.EnhanceObjectField;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.enhance.FieldLockType;
import com.chippy.redis.exception.CannotAcquireLockException;
import com.chippy.redis.exception.UnknownEnhanceObjectException;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 增强GET方法拦截器
 *
 * @author: chippy
 **/
public abstract class EnhanceGetMethodInterceptor extends DefaultEnhanceMethodInterceptor {

    public EnhanceGetMethodInterceptor(EnhanceObjectManager enhanceObjectManager) {
        super(enhanceObjectManager);
    }

    public Object process(Object sourceObject, Object proxyObject, Method method, MethodProxy proxyMethod,
        Object[] args) throws InvocationTargetException, IllegalAccessException {
        final String methodName = method.getName();
        final String fullClassName = sourceObject.getClass().getName();
        final EnhanceObject enhanceObject = (EnhanceObject)sourceObject;

        final String fieldName = super.getFieldName(methodName, EnhancerUtil.GET);
        if (enhanceObjectManager.getExcludeFieldList(fullClassName).contains(fieldName)) {
            return method.invoke(sourceObject, args);
        }
        final EnhanceObjectField enhanceObjectFiled =
            enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
        final boolean notEmpty = ObjectUtil.isNotEmpty(enhanceObjectFiled);
        final boolean isLock = enhanceObjectFiled.getIsLock();
        final boolean isSupportReadLock = FieldLockType.supportR(enhanceObjectFiled.getFieldLockType());
        if (notEmpty && isLock || isSupportReadLock) {
            return this.lockInvokeGet(method, fullClassName, enhanceObject, fieldName, enhanceObjectFiled);
        } else {
            return method.invoke(sourceObject, args);
        }
    }

    private Object lockInvokeGet(Method method, String fullClassName, EnhanceObject enhanceObject, String fieldName,
        EnhanceObjectField enhanceObjectFiled) {
        Lock readLock = null;
        try {
            readLock = this.getReadWriteLock(this.getLockKey(fullClassName, fieldName)).readLock();
            final boolean isReadLockHolder =
                readLock.tryLock(enhanceObjectFiled.getWaitLockTime(), TimeUnit.MILLISECONDS);
            if (!isReadLockHolder) {
                throw new CannotAcquireLockException(String
                    .format("获取[class:%s, field:%s]读锁时间超时[timeout:%s]", fullClassName, fieldName,
                        enhanceObjectFiled.getWaitLockTime()));
            }
            final String readValue = String.valueOf(this.getField(enhanceObject.getId(), fieldName));
            return EnhanceJSONUtil.toBean(readValue, method.getReturnType());
        } catch (Exception e) {
            throw new UnknownEnhanceObjectException(e.getMessage(), e);
        } finally {
            this.doUnLock(readLock);
        }
    }

    @Override
    public String getSupportMethod() {
        return EnhancerUtil.GET;
    }

    @Override
    public int getArgsSize() {
        return NumberConstant.ZERO_INT;
    }

    public abstract Object getField(String id, String fieldName);

    public abstract Map<String, String> getFields(String id);

}