package com.chippy.redis.enhance;

/**
 * 字段锁类型
 *
 * @author: chippy
 **/
public enum FieldLockType {

    WRITE_LOCK,
    READ_LOCK,
    ALL;

    public static boolean supportW(FieldLockType fieldLockType) {
        return fieldLockType == WRITE_LOCK || fieldLockType == ALL;
    }

    public static boolean supportR(FieldLockType fieldLockType) {
        // 是写锁可以拥有读锁
        return fieldLockType == READ_LOCK || fieldLockType == WRITE_LOCK || fieldLockType == ALL;
    }

}