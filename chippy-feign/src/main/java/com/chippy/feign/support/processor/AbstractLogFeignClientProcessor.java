package com.chippy.feign.support.processor;

import cn.hutool.json.JSONUtil;
import com.chippy.feign.support.registry.RequestElement;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志功能FeignClientHelper调用时的处理器
 *
 * @author: chippy
 * @datetime 2020/12/13 15:43
 */
@Slf4j
public abstract class AbstractLogFeignClientProcessor implements FeignClientProcessor {

    /*** 核心处理前的日志信息预先缓存 */
    private static final String processBeforeLogStr = "调用服务[%s]的方法[%s]参数[%s]";
    /*** 核心处理后的日志信息预先缓存 */
    private static final String processAfterLogStr = "调用服务[%s]的方法[%s]结果[%s]";
    /*** 核心处理异常的日志信息预先缓存 */
    private static final String processExceptionLogStr = "调用服务[%s]的方法[%s]异常[%s]";

    @Override
    public Object[] processBefore(RequestElement element, Object[] param) {
        if (log.isDebugEnabled()) {
            log.debug(String.format(processBeforeLogStr, element.getClassName(), element.getMethodName(),
                JSONUtil.toJsonStr(param)));
        }
        return param;
    }

    @Override
    public Object processAfter(RequestElement element, Object response) {
        if (log.isDebugEnabled()) {
            log.debug(String.format(processAfterLogStr, element.getClassName(), element.getClassName(),
                JSONUtil.toJsonStr(response)));
        }
        return response;
    }

    @Override
    public void processException(RequestElement element, Exception e) {
        if (log.isErrorEnabled()) {
            log.error(
                String.format(processExceptionLogStr, element.getClassName(), element.getClassName(), e.getMessage()));
            log.error("具体异常信息-" + e);
        }
    }

}
