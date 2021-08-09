package com.chippy.oss.configuration.core;

import com.chippy.oss.context.*;
import com.chippy.oss.predicate.OssPredicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * oss上下文核心自动配置类
 *
 * @author: chippy
 */
@EnableConfigurationProperties(OssSnapshotProperties.class)
@Configuration
public class OssContextAutoConfiguration {

    @Bean
    public GenericOssClientTemplate genericOssClientTemplate(List<OssHandler> ossHandlerList, OssSnapshot ossSnapshot,
        OssClientContext ossClientContext, GenericOssRequestContextAssembler genericOssRequestContextAssembler) {
        return new GenericOssClientTemplate(ossHandlerList, ossSnapshot, ossClientContext,
            genericOssRequestContextAssembler);
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

    @Bean
    public OssPredicateHandler ossPredicateHandler(List<OssPredicate> ossPredicateList) {
        return new OssPredicateHandler(ossPredicateList);
    }

    @ConditionalOnMissingBean
    @Bean
    public OssSnapshot ossSnapshot(OssSnapshotProperties ossSnapshotProperties) {
        return new OssLruSnapshot(ossSnapshotProperties);
    }

    @Bean
    public OssSnapshotHandler ossSnapshotHandler(OssSnapshot ossSnapshot) {
        return new OssSnapshotHandler(ossSnapshot);
    }

}
