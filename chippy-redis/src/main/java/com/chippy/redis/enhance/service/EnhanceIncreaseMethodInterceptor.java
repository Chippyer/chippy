package com.chippy.redis.enhance.service;

import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.enhance.EnhanceObjectField;
import com.chippy.redis.enhance.EnhanceObjectManager;
import com.chippy.redis.utils.EnhancerUtil;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 增强自增操作拦截器
 *
 * @author: chippy
 **/
public abstract class EnhanceIncreaseMethodInterceptor extends DefaultEnhanceMethodInterceptor {

    public EnhanceIncreaseMethodInterceptor(EnhanceObjectManager enhanceObjectManager) {
        super(enhanceObjectManager);
    }

    @Override
    public Object process(Object sourceObject, Object proxyObject, Method method, MethodProxy proxyMethod,
        Object[] args) {
        final String fullClassName = sourceObject.getClass().getName();
        final EnhanceObject enhanceObject = (EnhanceObject)sourceObject;
        final String fieldName = String.valueOf(args[0]);
        final EnhanceObjectField enhanceObjectFiled =
            super.enhanceObjectManager.getEnhanceObjectFiled(fullClassName, fieldName);
        if (ObjectsUtil.isEmpty(enhanceObjectFiled)) {
            return null;
        }

        final Object increaseValue = this.increaseField(enhanceObject.getId(), fieldName, args[1]);
        ReflectUtil.setFieldValue(sourceObject, enhanceObjectFiled.getField(), increaseValue);
        return increaseValue;
    }

    @Override
    public String getSupportMethod() {
        return EnhancerUtil.INCREASE;
    }

    @Override
    public int getArgsSize() {
        return NumberConstant.TWO_INT;
    }

    public abstract Object increaseField(String id, String fieldName, Object arg);

}