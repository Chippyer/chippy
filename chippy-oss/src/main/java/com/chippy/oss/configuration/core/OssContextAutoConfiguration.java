package com.chippy.oss.configuration.core;

import com.chippy.oss.context.GenericOssRequestContextAssembler;
import com.chippy.oss.context.OssClientTemplate;
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
    public OssClientTemplate ossClientTemplate() {
        return new OssClientTemplate();
    }

    @Bean
    public GenericOssRequestContextAssembler genericOssRequestContextAssembler(
        OssContextProperties ossContextProperties) {
        return new GenericOssRequestContextAssembler(ossContextProperties.getClientName(),
            ossContextProperties.getLevel(), ossContextProperties.getFileDir(), ossContextProperties.getUploadType());
    }

}
