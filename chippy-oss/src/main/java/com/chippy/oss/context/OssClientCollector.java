package com.chippy.oss.context;

import com.chippy.oss.client.OssClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * oss客户端收集器
 *
 * @author: chippy
 **/
public class OssClientCollector implements BeanPostProcessor {

    private OssClientContext ossClientContext;

    public OssClientCollector(OssClientContext ossClientContext) {
        this.ossClientContext = ossClientContext;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof OssClient) {
            final OssClient ossClient = (OssClient)bean;
            ossClientContext.register(ossClient);
        }
        return bean;
    }

}