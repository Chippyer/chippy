package com.chippy.redis.enhance.service;

import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.constants.NumberConstant;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.exception.EnhanceMethodInterceptorException;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 默认增强对象管理操作实现
 *
 * @author: chippy
 **/
@Slf4j
public abstract class DefaultEnhanceObjectService implements EnhanceObjectService {

    private List<EnhanceMethodInterceptor> enhanceMethodInterceptorList;

    public DefaultEnhanceObjectService(List<EnhanceMethodInterceptor> enhanceMethodInterceptorList) {
        this.enhanceMethodInterceptorList = enhanceMethodInterceptorList;
    }

    @Override
    public void enhance(EnhanceObject sourceObject) {
        this.validateEnhanceParam(sourceObject);
        final Field[] fields = ReflectUtil.getFields(sourceObject.getClass());
        for (Field field : fields) {
            final Object fieldValueObject = ReflectUtil.getFieldValue(sourceObject, field);
            this.getEnhanceSetMethodInterceptor()
                .setField(sourceObject.getId(), field.getName(), EnhanceJSONUtil.toJsonStr(fieldValueObject));
        }
    }

    @Override
    public <T> T get(String id, Class<T> sourceObjectClass) {
        this.validateGetParam(id, sourceObjectClass);
        final Map<String, String> fieldMap = this.getEnhanceGetMethodInterceptor().getFields(id);
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

    private EnhanceSetMethodInterceptor getEnhanceSetMethodInterceptor() {
        final EnhanceMethodInterceptor enhanceMethodInterceptor =
            this.getEnhanceMethodInterceptor(EnhancerUtil.SET, NumberConstant.ONE_INT);
        if (Objects.isNull(enhanceMethodInterceptor)) {
            throw new EnhanceMethodInterceptorException("[SET]操作增强器有且只能有一个");
        }
        return (EnhanceSetMethodInterceptor)enhanceMethodInterceptor;
    }

    private EnhanceGetMethodInterceptor getEnhanceGetMethodInterceptor() {
        final EnhanceMethodInterceptor enhanceMethodInterceptor =
            this.getEnhanceMethodInterceptor(EnhancerUtil.GET, NumberConstant.ZERO_INT);
        if (Objects.isNull(enhanceMethodInterceptor)) {
            throw new EnhanceMethodInterceptorException("[GET]操作增强器有且只能有一个");
        }
        return (EnhanceGetMethodInterceptor)enhanceMethodInterceptor;
    }

    private <T> T doCreateProxy(T sourceObject) {
        return EnhancerUtil.createProxy(sourceObject, (MethodInterceptor)(proxyObject, method, args, proxyMethod) -> {
            final EnhanceMethodInterceptor enhanceMethodInterceptor =
                this.getEnhanceMethodInterceptor(method.getName(), args.length);
            return Objects.isNull(enhanceMethodInterceptor) ? method.invoke(sourceObject, args) :
                enhanceMethodInterceptor.process(sourceObject, proxyObject, method, proxyMethod, args);
        });
    }

    private EnhanceMethodInterceptor getEnhanceMethodInterceptor(String methodName, int argsLength) {
        for (EnhanceMethodInterceptor enhanceMethodInterceptor : enhanceMethodInterceptorList) {
            final boolean isStartWith = methodName.startsWith(enhanceMethodInterceptor.getSupportMethod());
            final boolean isArgsEq = Objects.equals(enhanceMethodInterceptor.getArgsSize(), argsLength);
            if (isStartWith && isArgsEq) {
                return enhanceMethodInterceptor;
            }
        }
        return null;
    }

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

}