package com.chippy.redis.enhance;

/**
 * 增强对象操作服务
 *
 * @author: chippy
 **/
public interface EnhanceObjectService {

    /**
     * 对源对象进行增强
     *
     * @param sourceObject 源对象
     * @author chippy
     */
    void enhance(EnhanceObject sourceObject);

    /**
     * 获取增强对象
     *
     * @param id                对象唯一标识（重复则会覆盖）
     * @param sourceObjectClass 源对象类型（未执行{@link EnhanceObjectService#enhance}操作的话返回为控）
     * @return 增强对象
     * @author chippy
     */
    <T> T get(String id, Class<T> sourceObjectClass);

}