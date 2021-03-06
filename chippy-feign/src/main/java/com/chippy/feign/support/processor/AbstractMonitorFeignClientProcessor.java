package com.chippy.feign.support.processor;

import com.chippy.feign.support.registry.RequestElement;
import lombok.extern.slf4j.Slf4j;

/**
 * 监控整体请求时间功能FeignClientHelper调用时的处理器
 * 为了确保监控数据的有效性，我们实现此类并托管给Spring IOC
 * 且保证使用{@link org.springframework.core.annotation.Order}
 * 注解的value()值小于其他的{@link FeignClientProcessor}处理器
 * 保证顺序优先，因为我们调度{@link FeignClientProcessor}时采用倒序执行
 *
 * @author: chippy
 */
@Slf4j
public abstract class AbstractMonitorFeignClientProcessor implements FeignClientProcessor {

    private final ThreadLocal<Long> monitorThreadLocal = new ThreadLocal<>();

    @Override
    public Object[] processBefore(RequestElement element, Object[] param) {
        monitorThreadLocal.set(System.currentTimeMillis());
        return param;
    }

    @Override
    public Object processAfter(RequestElement element, Object response) {
        final long feignClientRequestStartTimeMs = monitorThreadLocal.get();
        final long feignClientRequestEndTimeMs = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("当前请求-[" + element.getClassName() + "]-[" + element.getMethodName() + "]-耗时-" + (
                feignClientRequestEndTimeMs - feignClientRequestStartTimeMs));
            monitorThreadLocal.remove();
        }
        return response;
    }

}
