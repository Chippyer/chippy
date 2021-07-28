package com.chippy.feign.support.processor;

import com.chippy.feign.support.registry.RequestElement;

import java.util.List;

/**
 * FeignClientHelper调用时的处理器
 *
 * @author: chippy
 */
public interface FeignClientProcessor {

    /**
     * 获取指定拦截路径规则
     *
     * @return 包含的请求路径
     * @author chippy
     */
    List<String> getIncludePathPattern();

    /**
     * 获取指定排除的拦截路径规则
     *
     * @return 不包含的请求路径
     * @author chippy
     */
    default List<String> getExcludePathPattern() {
        return null;
    }

    /**
     * 调用前的自定义操作
     *
     * @param element 请求元素信息
     * @param param   请求参数
     * @return 处理后的请求参数
     * @author chippy
     */
    Object[] processBefore(RequestElement element, Object[] param);

    /**
     * 调用后的自定义操作
     * 注意：这里如果想要包装data中的数据，一定要进行判空
     *
     * @param element  请求元素信息
     * @param response 响应结果|
     * @return 处理后的响应结果
     * @author chippy
     */
    Object processAfter(RequestElement element, Object response);

    /**
     * 调用异常的自定义操作
     *
     * @param element 请求元素信息
     * @param e       异常信息
     * @author chippy
     */
    void processException(RequestElement element, Throwable e);

}
