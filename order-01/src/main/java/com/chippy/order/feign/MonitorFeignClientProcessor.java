package com.chippy.order.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.chippy.feign.exception.FeignClientRequestException;
import com.chippy.feign.support.processor.AbstractMonitorFeignClientProcessor;
import com.chippy.feign.support.registry.RequestElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监控性能指标扩展处理器
 *
 * @author: chippy
 * @datetime 2021/7/23 23:12
 */
@Service
@Slf4j
@Order
public class MonitorFeignClientProcessor extends AbstractMonitorFeignClientProcessor {

    @Override
    public List<String> getIncludePathPattern() {
        return CollectionUtil.toList("/**");
    }

    @Override
    public void processException(RequestElement element, Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("监控性能指标扩展处理器异常-[{}]", e.getMessage(), e);
        }
        throw new FeignClientRequestException(e.getMessage(), e);
    }

}
