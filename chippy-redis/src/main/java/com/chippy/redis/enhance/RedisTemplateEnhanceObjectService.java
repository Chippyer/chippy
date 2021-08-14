package com.chippy.redis.enhance;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于{@link org.springframework.data.redis.core.StringRedisTemplate}实现的增强对象操作实现
 *
 * @author: chippy
 **/
public class RedisTemplateEnhanceObjectService extends DefaultEnhanceObjectService {

    private StringRedisTemplate stringRedisTemplate;

    public RedisTemplateEnhanceObjectService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doSetField(String id, String fieldName, String fieldValue) {
        stringRedisTemplate.opsForHash().put(id, fieldName, fieldValue);
    }

    @Override
    protected Map<String, String> doGetField(String id) {
        final Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(id);
        final Map<String, String> fieldMap = new HashMap<>(entries.size());
        entries.forEach((fieldName, fieldValue) -> fieldMap.put(String.valueOf(fieldName), String.valueOf(fieldValue)));
        return fieldMap;
    }

}