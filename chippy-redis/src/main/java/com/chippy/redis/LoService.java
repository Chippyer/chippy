package com.chippy.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * lo service
 *
 * @author: chippy
 **/
public class LoService {

    private RedisTemplate<String, String> redisTemplate;

    private Map<Class /* source object class */, Class /* proxy object class */> classCache = new HashMap<>();
    private Map<String /* id */ , Map<String, Object> /* object detail fieldName:fieldValue */> simulationRedis =
        new HashMap<>();

    public static void main(String[] args) {
        final TestBean testBean = new TestBean();
        testBean.setName("chippy");
        final LoService loService = new LoService();
        final TestBean delegate = (TestBean)loService.delegate("a", testBean);
        System.out.println("delegate proxy-" + delegate.getName());

        System.out.println("--------------------------------");

        final TestBean proxy = loService.get("a", TestBean.class);
        System.out.println(proxy.getName());
    }

    public Object delegate(String id, Object sourceObject) {
        final Class<?> sourceClass = sourceObject.getClass();
        final Field[] sourceObjectFields = ReflectUtil.getFields(sourceClass);

        final Class proxySourceClass = classCache.get(sourceClass);
        if (ObjectsUtil.isNotEmpty(proxySourceClass)) {
            final Object newProxyObject = ReflectUtil.newInstance(proxySourceClass);
            final Map<String, Object> map = new HashMap<>();
            for (Field field : sourceObjectFields) {
                map.put(field.getName(), ReflectUtil.getFieldValue(newProxyObject, field));
            }
            simulationRedis.put(id, map);
            // todo 待优化
            return BeanUtil.mapToBean(map, proxySourceClass, Boolean.TRUE);
        }

        final Object newProxyObject = this.createProxy(sourceObject);
        final Map<String, Object> map = new HashMap<>();
        for (Field field : sourceObjectFields) {
            map.put(field.getName(), ReflectUtil.getFieldValue(sourceObject, field));
        }
        classCache.put(sourceClass, newProxyObject.getClass());
        simulationRedis.put(id, map);
        return newProxyObject;
    }

    public <T> T get(String id, Class<T> sourceObjectClass) {
        final Map<String, Object> simulationMap = simulationRedis.get(id);
        if (ObjectsUtil.isEmpty(simulationMap)) {
            return null;
        }

        Object newProxyObject;
        final Class proxyObjectClass = classCache.get(sourceObjectClass);
        if (ObjectsUtil.isNotEmpty(proxyObjectClass)) {
            newProxyObject = ReflectUtil.newInstance(proxyObjectClass);
        } else {
            newProxyObject = this.createProxy(ReflectUtil.newInstance(sourceObjectClass));
        }

        final Field[] fields = ReflectUtil.getFields(proxyObjectClass);
        for (Field field : fields) {
            final Object fieldValue = simulationMap.get(field.getName());
            if (ObjectsUtil.isNotEmpty(fieldValue)) {
                ReflectUtil.setFieldValue(newProxyObject, field, fieldValue);
            }
        }
        return (T)newProxyObject;
    }

    private Object createProxy(Object object) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback((MethodInterceptor)(obj, method, args, proxy) -> {
            System.out.println(String.format("method name-[%s]", method.getName()));
            return method.invoke(object, args);
        });
        return enhancer.create();
    }

}