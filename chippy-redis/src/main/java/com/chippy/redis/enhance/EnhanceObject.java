package com.chippy.redis.enhance;

/**
 * 增强对象
 *
 * @author: chippy
 **/
public interface EnhanceObject {

    /**
     * 增强对象唯一标识
     *
     * @return 对象唯一标识
     * @author chippy
     */
    String getId();

    /**
     * 自增值(整形类型)
     *
     * @param fileName 自增属性
     * @param value    自增值
     * @author chippy
     */
    default void increase(String fileName, Long value) {
    }

    /**
     * 自增值(浮点类型)
     *
     * @param fileName 自增属性
     * @param value    自增值
     * @author chippy
     */
    default void increase(String fileName, Double value) {
    }

}