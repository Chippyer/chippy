package com.chippy.redis.enhance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.exception.CannotAcquireLockException;
import com.chippy.redis.exception.UnknownEnhanceObjectException;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认增强对象管理操作实现
 * 默认实现单机锁{@link ReentrantReadWriteLock}
 *
 * @author: chippy
 **/
@Slf4j
public abstract class DefaultEnhanceObjectService implements EnhanceObjectService, ELock {

    private EnhanceObjectManager enhanceObjectManager;

    public DefaultEnhanceObjectService(EnhanceObjectManager enhanceObjectManager) {
        this.enhanceObjectManager = enhanceObjectManager;
    }

    @Override
    public void enhance(EnhanceObject sourceObject) {
        this.validateEnhanceParam(sourceObject);
        final Field[] fields = ReflectUtil.getFields(sourceObject.getClass());
        for (Field field : fields) {
            final Object fieldValueObject = ReflectUtil.getFieldValue(sourceObject, field);
            this.setField(sourceObject.getId(), field.getName(), EnhanceJSONUtil.toJsonStr(fieldValueObject));
        }
    }

    @Override
    public <T> T get(String id, Class<T> sourceObjectClass) {
        this.validateGetParam(id, sourceObjectClass);
        final Map<String, String> fieldMap = this.getField(id);
        if (ObjectsUtil.isEmpty(fieldMap)) {
            return null;
        }

        final T sourceObject = ReflectUtil.newInstance(sourceObjectClass);
        final Field[] fields = sourceObjectClass.getDeclaredFields();
        for (Field field : fields) {
            final Object fieldValue = EnhanceJSONUtil.toBean(fieldMap.get(field.getName()), field.getType());
            ReflectUtil.setFieldValue(sourceObject, field, fieldValue);
        }
        return this.doCreateProxy(sourceObject);
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return new ReentrantReadWriteLock();
    }

    private <T> T doCreateProxy(T sourceObject) {
        return EnhancerUtil.createProxy(sourceObject, (MethodInterceptor)(obj, method, args, proxy) -> {
            final String methodName = method.getName();
            final String fullClassName = sourceObject.getClass().getName();
            final EnhanceObject enhanceObject = (EnhanceObject)obj;

            if (methodName.startsWith(EnhancerUtil.SET) && ObjectUtil.isNotEmpty(args)) {
                final String fieldName = this.getFieldName(methodName, EnhancerUtil.SET);
                if (enhanceObjectManager.getExcludeFieldList(fullClassName).contains(fieldName)) {
                    return this.normalInvokeSet(sourceObject, method, args, enhanceObject, fieldName);
                }

                final EnhanceObjectField enhanceObjectFiled =
                    enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
                final boolean notEmpty = ObjectUtil.isNotEmpty(enhanceObjectFiled);
                final boolean isLock = enhanceObjectFiled.getIsLock();
                final boolean isSupportWriteLock = FieldLockType.supportW(enhanceObjectFiled.getFieldLockType());
                if (notEmpty && isLock && isSupportWriteLock) {
                    return this
                        .lockInvokeSet(sourceObject, method, enhanceObject, fullClassName, enhanceObjectFiled, args[0]);
                } else {
                    return this.normalInvokeSet(sourceObject, method, args, enhanceObject, fieldName);
                }
            } else if (methodName.startsWith(EnhancerUtil.GET) && ObjectUtil.isEmpty(args)) {
                final String fieldName = this.getFieldName(methodName, EnhancerUtil.GET);
                if (enhanceObjectManager.getExcludeFieldList(fullClassName).contains(fieldName)) {
                    return this.normalInvokeGet(sourceObject, method, args);
                }

                final EnhanceObjectField enhanceObjectFiled =
                    enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
                final boolean notEmpty = ObjectUtil.isNotEmpty(enhanceObjectFiled);
                final boolean isLock = enhanceObjectFiled.getIsLock();
                final boolean isSupportReadLock = FieldLockType.supportR(enhanceObjectFiled.getFieldLockType());
                if (notEmpty && isLock || isSupportReadLock) {
                    return this.lockInvokeGet(method, fullClassName, enhanceObject, fieldName, enhanceObjectFiled);
                } else {
                    return this.normalInvokeGet(sourceObject, method, args);
                }
            } else {
                // 非GET SET属性按照通常方式处理，不做加工
                return this.normalInvoke(sourceObject, method, args);
            }
        });
    }

    private <T> Object normalInvokeSet(T sourceObject, Method method, Object[] args, EnhanceObject enhanceObject,
        String fieldName) throws IllegalAccessException, InvocationTargetException {
        this.doEnhanceSet(enhanceObject, fieldName, args[0]);
        return method.invoke(sourceObject, args);
    }

    private <T> Object normalInvokeGet(T sourceObject, Method method, Object[] args)
        throws IllegalAccessException, InvocationTargetException {
        return this.normalInvoke(sourceObject, method, args);
    }

    private <T> Object normalInvoke(T sourceObject, Method method, Object[] args)
        throws IllegalAccessException, InvocationTargetException {
        return method.invoke(sourceObject, args);
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
            final String readValue = String.valueOf(this.doEnhanceGet(enhanceObject, fieldName));
            return EnhanceJSONUtil.toBean(readValue, method.getReturnType());
        } catch (Exception e) {
            throw new UnknownEnhanceObjectException(e.getMessage(), e);
        } finally {
            this.doUnLock(readLock);
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
            final String writeValue = this.getFinalWriteValue(enhanceObject, enhanceObjectFiled, fieldName, arg);
            return method.invoke(sourceObject, EnhanceJSONUtil.toBean(writeValue, arg.getClass()));
        } catch (Exception e) {
            throw new UnknownEnhanceObjectException(e.getMessage(), e);
        } finally {
            if (Objects.nonNull(writeLock)) {
                this.doUnLock(writeLock);
            }
        }
    }

    private String getFieldName(String methodName, String get) {
        return EnhancerUtil.lowerFirstCase(methodName.substring(get.length()));
    }

    private String getFinalWriteValue(EnhanceObject enhanceObject, EnhanceObjectField enhanceObjectFiled,
        String fieldName, Object args) {
        return this.isNumberType(enhanceObjectFiled) ?
            String.valueOf(this.doEnhanceIncrease(enhanceObject, fieldName)) :
            String.valueOf(this.doEnhanceSet(enhanceObject, fieldName, args));
    }

    private void doUnLock(Lock writeLock) {
        if (writeLock instanceof RLock) {
            final RLock rWriteLock = (RLock)writeLock;
            if (rWriteLock.isHeldByCurrentThread()) {
                rWriteLock.unlock();
            }
        } else {
            writeLock.unlock();
        }
    }

    private boolean isNumberType(EnhanceObjectField enhanceObjectFiled) {
        final boolean isDouble = Objects.equals(enhanceObjectFiled.getField().getType(), Double.class);
        final boolean isLong = Objects.equals(enhanceObjectFiled.getField().getType(), Long.class);
        final boolean isFloat = Objects.equals(enhanceObjectFiled.getField().getType(), Float.class);
        final boolean isInteger = Objects.equals(enhanceObjectFiled.getField().getType(), Integer.class);
        final boolean isShort = Objects.equals(enhanceObjectFiled.getField().getType(), Short.class);
        return isDouble || isLong || isFloat || isInteger || isShort;
    }

    private Object doEnhanceIncrease(EnhanceObject enhanceObject, String fieldName) {
        return this.increaseField(enhanceObject.getId(), fieldName);
    }

    private Object doEnhanceSet(EnhanceObject enhanceObject, String fieldName, Object arg) {
        this.setField(enhanceObject.getId(), fieldName, EnhanceJSONUtil.toJsonStr(arg));
        return arg;
    }

    private Object doEnhanceGet(EnhanceObject enhanceObject, String fieldName) {
        final Map<String, String> fieldMap = this.getField(enhanceObject.getId());
        final Set<String> keySet = fieldMap.keySet();
        for (String field : keySet) {
            if (Objects.equals(field, fieldName)) {
                return fieldMap.get(field);
            }
        }
        return null;
    }

    protected abstract Object increaseField(String id, String fieldName);

    protected abstract void setField(String id, String fieldName, String fieldValue);

    protected abstract Map<String, String> getField(String id);

    private void validateEnhanceParam(EnhanceObject sourceObject) {
        if (ObjectsUtil.isEmpty(sourceObject)) {
            throw new IllegalArgumentException("待增强对象为空");
        }
        if (ObjectsUtil.isEmpty(sourceObject.getId())) {
            throw new IllegalArgumentException("待增强对象唯一标识不能为空");
        }
    }

    private <T> void validateGetParam(String id, Class<T> sourceObjectClass) {
        if (ObjectsUtil.isEmpty(id)) {
            throw new IllegalArgumentException("增强对象唯一标识不能为空");
        }
        if (ObjectsUtil.isEmpty(sourceObjectClass)) {
            throw new IllegalArgumentException("增强对象类型不能为空");
        }
    }

    private String getLockKey(String fullClassName, EnhanceObjectField enhanceObjectFiled) {
        return this.getLockKey(fullClassName, enhanceObjectFiled.getField().getName());
    }

    private String getLockKey(String fullClassName, String fieldName) {
        return fullClassName + fieldName;
    }

}