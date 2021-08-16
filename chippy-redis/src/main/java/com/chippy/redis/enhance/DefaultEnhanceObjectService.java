package com.chippy.redis.enhance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.exception.CannotAcquireLockException;
import com.chippy.redis.exception.UnknownEnhanceObjectException;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
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

    private static final String GET_ID_METHOD_NAME = "getId";
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
            this.doSetField(sourceObject.getId(), field.getName(), EnhanceJSONUtil.toJsonStr(fieldValueObject));
        }
    }

    @Override
    public <T> T get(String id, Class<T> sourceObjectClass) {
        this.validateGetParam(id, sourceObjectClass);
        final Map<String, String> fieldMap = this.doGetField(id);
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
            final EnhanceObject enhanceObject = (EnhanceObject)obj;
            final String fullClassName = sourceObject.getClass().getName();

            if (methodName.startsWith(EnhancerUtil.SET) && ObjectUtil.isNotEmpty(args)) {
                // fieldName = setA -> a;
                final String fieldName = EnhancerUtil.lowerFirstCase(methodName.substring(EnhancerUtil.SET.length()));
                final EnhanceObjectField enhanceObjectFiled =
                    enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
                final Object arg = args[0];

                if (Objects.nonNull(enhanceObjectFiled) && enhanceObjectFiled.getIsLock()) {
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

                        this.enhanceSet(enhanceObject, enhanceObjectFiled, fieldName, arg);
                        final String writeValue =
                            String.valueOf(this.enhanceGet(enhanceObject, enhanceObjectFiled, fieldName));
                        return method.invoke(sourceObject, EnhanceJSONUtil.toBean(writeValue, arg.getClass()));
                    } catch (Exception e) {
                        throw new UnknownEnhanceObjectException(e.getMessage(), e);
                    } finally {
                        if (Objects.nonNull(writeLock)) {
                            writeLock.unlock();
                        }
                    }
                } else {
                    return method.invoke(sourceObject, arg);
                }
            } else if (methodName.startsWith(EnhancerUtil.GET)) {
                // todo 抽取出来一个过滤行为
                if (Objects.equals(methodName, GET_ID_METHOD_NAME)) {
                    return method.invoke(sourceObject, args);
                }

                // fieldName = setA -> a;
                final String fieldName = EnhancerUtil.lowerFirstCase(methodName.substring(EnhancerUtil.GET.length()));
                final EnhanceObjectField enhanceObjectFiled =
                    enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
                if (Objects.nonNull(enhanceObjectFiled) && enhanceObjectFiled.getIsLock()) {
                    Lock readLock = null;
                    try {
                        readLock = this.getReadWriteLock(this.getLockKey(fullClassName, fieldName)).writeLock();
                        final boolean isReadLockHolder =
                            readLock.tryLock(enhanceObjectFiled.getWaitLockTime(), TimeUnit.MILLISECONDS);
                        if (!isReadLockHolder) {
                            throw new CannotAcquireLockException(String
                                .format("获取[class:%s, field:%s]读锁时间超时[timeout:%s]", fullClassName, fieldName,
                                    enhanceObjectFiled.getWaitLockTime()));
                        }

                        final String readValue =
                            String.valueOf(this.enhanceGet(enhanceObject, enhanceObjectFiled, fieldName));
                        return EnhanceJSONUtil.toBean(readValue, method.getReturnType());
                    } catch (Exception e) {
                        throw new UnknownEnhanceObjectException(e.getMessage(), e);
                    } finally {
                        if (Objects.nonNull(readLock)) {
                            readLock.unlock();
                        }
                    }
                } else {
                    return method.invoke(sourceObject, args);
                }
            } else {
                return method.invoke(sourceObject, args);
            }
        });
    }

    private void enhanceSet(EnhanceObject enhanceObject, EnhanceObjectField enhanceObjectFiled, String fieldName,
        Object value) {
        if (this.isNumberType(enhanceObjectFiled)) {
            this.doIncrease(enhanceObject, fieldName);
        } else {
            this.doSet(enhanceObject, fieldName, value);
        }
    }

    private Object enhanceGet(EnhanceObject enhanceObject, EnhanceObjectField enhanceObjectFiled, String fieldName) {
        return this.doGet(enhanceObject, fieldName);
    }

    private boolean isNumberType(EnhanceObjectField enhanceObjectFiled) {
        return Objects.equals(enhanceObjectFiled.getField().getType(), Double.class) || Objects
            .equals(enhanceObjectFiled.getField().getType(), Long.class) || Objects
            .equals(enhanceObjectFiled.getField().getType(), Float.class) || Objects
            .equals(enhanceObjectFiled.getField().getType(), Integer.class) || Objects
            .equals(enhanceObjectFiled.getField().getType(), Short.class);
    }

    private void doIncrease(EnhanceObject enhanceObject, String fieldName) {
        this.doIncreaseField(enhanceObject.getId(), fieldName);
    }

    private void doSet(EnhanceObject enhanceObject, String fieldName, Object arg) {
        this.doSetField(enhanceObject.getId(), fieldName, EnhanceJSONUtil.toJsonStr(arg));
    }

    private Object doGet(EnhanceObject enhanceObject, String fieldName) {
        final Map<String, String> fieldMap = this.doGetField(enhanceObject.getId());
        final Set<String> keySet = fieldMap.keySet();
        for (String field : keySet) {
            if (Objects.equals(field, fieldName)) {
                return fieldMap.get(field);
            }
        }
        return null;
    }

    protected abstract void doIncreaseField(String id, String fieldName);

    protected abstract void doSetField(String id, String fieldName, String fieldValue);

    protected abstract Map<String, String> doGetField(String id);

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