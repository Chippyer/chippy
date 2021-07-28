package com.chippy.common.result;

/**
 * 服务间通讯返回结果结构接口
 *
 * @author: chippy
 */
public interface Result<T> {

    /**
     * 成功的Code编码值
     *
     * @author chippy
     */
    int definitionSuccessCode();

    /**
     * 通讯返回编码值
     *
     * @author chippy
     */
    int getCode();

    /**
     * 设置通讯返回结果内容
     *
     * @param data 通讯返回结果
     * @author chippy
     */
    void setData(T data);

    /**
     * 通讯返回结果内容
     *
     * @author chippy
     */
    T getData();

    /**
     * 通讯返回错误信息
     *
     * @author chippy
     */
    String getErrorMsg();

}
