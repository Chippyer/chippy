package com.chippy.redis.enhance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
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

    private <T> T doCreateProxy(T sourceObject) {
        return EnhancerUtil.createProxy(sourceObject, (MethodInterceptor)(obj, method, args, proxy) -> {
            final String methodName = method.getName();
            final EnhanceObject enhanceObject = (EnhanceObject)obj;
            final String fullClassName = sourceObject.getClass().getName();
            // fieldName = setA -> a;
            final String fieldName = EnhancerUtil.lowerFirstCase(methodName.substring(EnhancerUtil.SET.length()));

            if (methodName.startsWith(EnhancerUtil.SET) && ObjectUtil.isNotEmpty(args)) {
                this.enhanceSet(enhanceObject, fullClassName, fieldName, args[0]);
                return method.invoke(sourceObject, args);
            } else if (methodName.startsWith(EnhancerUtil.GET)) {
                this.enhanceGet(enhanceObject, fullClassName, fieldName);
                return method.invoke(sourceObject, args);
            } else {
                // 忽略非get set行为
                return method.invoke(sourceObject, args);
            }
        });
    }

    private void enhanceSet(EnhanceObject enhanceObject, String fullClassName, String fieldName, Object value) {
        final EnhanceObjectField enhanceObjectFiled =
            enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
        if (Objects.nonNull(enhanceObjectFiled) && enhanceObjectFiled.getIsLock()) {
            Lock writeLock = null;
            try {
                writeLock = this.getReadWriteLock(this.getLockKey(fullClassName, enhanceObjectFiled)).writeLock();
                final boolean isHolderWriteLock =
                    writeLock.tryLock(enhanceObjectFiled.getWaitLockTime(), TimeUnit.MILLISECONDS);
                if (!isHolderWriteLock) {
                    log.error("获取[class:{}, field:{}]写锁失败", fullClassName, fieldName);
                }
                this.doSet(enhanceObject, fieldName, value);
            } catch (Exception ignore) {
                // 忽略任何异常信息
            } finally {
                if (Objects.nonNull(writeLock)) {
                    writeLock.unlock();
                }
            }
        } else {
            this.doSet(enhanceObject, fieldName, value);
        }
    }

    private void enhanceGet(EnhanceObject enhanceObject, String fullClassName, String fieldName) {
        final EnhanceObjectField enhanceObjectFiled =
            enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
        if (Objects.nonNull(enhanceObjectFiled) && enhanceObjectFiled.getIsLock()) {
            Lock readLock = null;
            try {
                readLock = this.getReadWriteLock(this.getLockKey(fullClassName, enhanceObjectFiled)).readLock();
                final boolean isHolderReadLock =
                    readLock.tryLock(enhanceObjectFiled.getWaitLockTime(), TimeUnit.MILLISECONDS);
                if (!isHolderReadLock) {
                    log.error("获取[class:{}, field:{}]读锁失败", fullClassName, enhanceObjectFiled.getField().getName());
                }
                this.doGet(enhanceObject, fieldName);
            } catch (Exception e) {
                // 忽略任何异常信息
            } finally {
                if (Objects.nonNull(readLock)) {
                    readLock.unlock();
                }
            }
        } else {
            this.doGet(enhanceObject, fieldName);
        }
    }

    private void doSet(EnhanceObject enhanceObject, String fieldName, Object arg) {
        this.doSetField(enhanceObject.getId(), fieldName, EnhanceJSONUtil.toJsonStr(arg));
    }

    private void doGet(EnhanceObject enhanceObject, String fieldName) {
        // 暂时不做任何事情
    }

    @Override
    public ReadWriteLock getReadWriteLock(String lockKey) {
        return new ReentrantReadWriteLock();
    }

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
        return fullClassName + enhanceObjectFiled.getField().getName();
    }

}