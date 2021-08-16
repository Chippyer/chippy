package com.chippy.redis.enhance;

import lombok.Data;

import java.util.List;

/**
 * 增强对象信息
 *
 * @author: chippy
 **/
@Data
public class EnhanceObjectInfo {

    /**
     * 类名简称
     */
    private String className;

    /**
     * 全限定类名
     */
    private String fullClassName;

    /**
     * 类实例信息
     */
    private Class<?> classType;

    /**
     * 增强属性信息集合{@link EnhanceObjectField}
     */
    private List<EnhanceObjectField> enhanceObjectFieldList;

    /**
     * 忽略增强的属性
     */
    private List<String> excludeFieldList;

}