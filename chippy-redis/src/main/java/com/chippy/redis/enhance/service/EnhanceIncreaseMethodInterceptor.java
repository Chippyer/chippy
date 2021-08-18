package com.chippy.redis.enhance.service;

import com.chippy.redis.constants.NumberConstant;
import com.chippy.redis.enhance.EnhanceObject;
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
        final EnhanceObject enhanceObject = (EnhanceObject)sourceObject;
        return this.increaseField(enhanceObject.getId(), String.valueOf(args[0]), args[1]);
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