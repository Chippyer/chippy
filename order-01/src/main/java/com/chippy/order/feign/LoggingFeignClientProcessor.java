package com.chippy.order.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.chippy.user.support.processor.AbstractLogFeignClientProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志打印请求扩展处理
 *
 * @author: chippy
 * @datetime 2021/7/23 22:58
 */
@Service
@Order(1)
public class LoggingFeignClientProcessor extends AbstractLogFeignClientProcessor {

    @Override
    public List<String> getIncludePathPattern() {
        return CollectionUtil.toList("/**");
    }

}
