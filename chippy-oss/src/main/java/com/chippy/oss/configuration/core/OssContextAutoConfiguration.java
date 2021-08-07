package com.chippy.oss.configuration.core;

import com.chippy.oss.context.GenericOssClientTemplate;
import com.chippy.oss.context.GenericOssRequestContextAssembler;
import com.chippy.oss.context.OssClientCollector;
import com.chippy.oss.context.OssClientContext;
import com.chippy.oss.predicate.OssPredicateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss上下文核心自动配置类
 *
 * @author: chippy
 */
@Configuration
public class OssContextAutoConfiguration {

    @Bean
    public GenericOssClientTemplate genericOssClientTemplate(OssPredicateHandler ossPredicateHandler,
        OssClientContext ossClientContext, GenericOssRequestContextAssembler genericOssRequestContextAssembler) {
        return new GenericOssClientTemplate(ossPredicateHandler, ossClientContext, genericOssRequestContextAssembler);
    }

    @Bean
    public GenericOssRequestContextAssembler genericOssRequestContextAssembler() {
        return new GenericOssRequestContextAssembler();
    }

    @Bean
    public OssClientContext ossClientContext() {
        return new OssClientContext();
    }

    @Bean
    public OssClientCollector ossClientCollector(OssClientContext ossClientContext) {
        return new OssClientCollector(ossClientContext);
    }

}
