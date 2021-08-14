package com.chippy.redis.enhance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.redis.utils.EnhanceJSONUtil;
import com.chippy.redis.utils.EnhancerUtil;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 默认增强对象管理操作实现
 *
 * @author: chippy
 **/
public abstract class DefaultEnhanceObjectService implements EnhanceObjectService {

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
            final EnhanceObject delegateObject = (EnhanceObject)obj;
            if (methodName.startsWith(EnhancerUtil.SET) && ObjectUtil.isNotEmpty(args)) {
                final String finalMethodName =
                    EnhancerUtil.lowerFirstCase(methodName.substring(EnhancerUtil.SET.length()));
                this.doSetField(delegateObject.getId(), finalMethodName, EnhanceJSONUtil.toJsonStr(args[0]));
                return method.invoke(sourceObject, args);
            } else if (methodName.startsWith(EnhancerUtil.GET)) {
                // ignore
                return method.invoke(sourceObject, args);
            } else {
                // ignore
                return method.invoke(sourceObject, args);
            }
        });
    }

    /**
     * 设置属性
     *
     * @param id         增强对象唯一标识
     * @param fieldName  属性名称
     * @param fieldValue 属性值
     * @author chippy
     */
    protected abstract void doSetField(String id, String fieldName, String fieldValue);

    /**
     * 获取属性
     *
     * @param id 增强对象唯一标识
     * @return 增强对象所有属性值
     * @author chippy
     */
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

}