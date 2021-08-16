package com.chippy.redis.enhance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性锁标识
 *
 * @author: chippy
 **/
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldLock {

    long waitLockTime() default 1000;

    FieldLockType fieldLockType() default FieldLockType.WRITE_LOCK;

}