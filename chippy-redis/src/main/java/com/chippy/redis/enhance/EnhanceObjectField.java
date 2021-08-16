package com.chippy.redis.enhance;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * 增强对象属性信息
 *
 * @author: chippy
 **/
@Data
public class EnhanceObjectField {

    /**
     * 等待获取锁的时间
     */
    private long waitLockTime;

    /**
     * 属性信息
     */
    private Field field;

    /**
     * 是否加锁
     */
    private Boolean isLock;

    /**
     * 加锁定额类型
     */
    private FieldLockType fieldLockType;

}