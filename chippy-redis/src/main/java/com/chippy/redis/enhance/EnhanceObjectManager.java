package com.chippy.redis.enhance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增强对象所信息管理器
 *
 * @author: chippy
 **/
public class EnhanceObjectManager {

    private static final String MARK = "#";

    public EnhanceObjectManager(int capacity) {
        enhanceObjectInfoMap = new HashMap<>(capacity);
        enhanceObjectFieldMap = new HashMap<>(capacity << 3);
    }

    private Map<String /* full class name */, EnhanceObjectInfo> enhanceObjectInfoMap;
    private Map<String /* full class name + field name */, EnhanceObjectField> enhanceObjectFieldMap;

    void register(String fullClassName, EnhanceObjectInfo enhanceObjectInfo) {
        enhanceObjectInfoMap.put(fullClassName, enhanceObjectInfo);
        final List<EnhanceObjectField> enhanceObjectFieldList = enhanceObjectInfo.getEnhanceObjectFieldList();
        for (EnhanceObjectField enhanceObjectField : enhanceObjectFieldList) {
            final String fieldKey = this.getFieldKey(fullClassName, enhanceObjectField.getField().getName());
            enhanceObjectFieldMap.put(fieldKey, enhanceObjectField);
        }
    }

    public EnhanceObjectInfo getEnhanceObjectInfo(Class classType) {
        return enhanceObjectInfoMap.get(classType.getName());
    }

    public EnhanceObjectInfo getEnhanceObjectInfo(String fullClassName) {
        return enhanceObjectInfoMap.get(fullClassName);
    }

    public EnhanceObjectField getEnhanceObjectFiled(String fullClassName, String filedName) {
        String fieldKey = this.getFieldKey(fullClassName, filedName);
        return enhanceObjectFieldMap.get(fieldKey);
    }

    private String getFieldKey(String fullClassName, String name) {
        return fullClassName + MARK + name;
    }

}