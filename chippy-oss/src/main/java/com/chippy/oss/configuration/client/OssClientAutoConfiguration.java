package com.chippy.oss.configuration.client;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.chippy.oss.client.AliClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * oss客户端自动配置类
 *
 * @author: chippy
 */
@EnableConfigurationProperties(AliClientProperties.class)
@Configuration
public class OssClientAutoConfiguration {

    @Resource
    private OssClientContext ossClientContext;

    @Bean
    public AliClient aliClient(AliClientProperties aliClientProperties) {
        final OSS ossClient = new OSSClientBuilder()
            .build(aliClientProperties.getEndpoint(), aliClientProperties.getAccessId(),
                aliClientProperties.getAccessKey());
        final AliClient aliClient = new AliClient(ossClient, aliClientProperties);
        ossClientContext.register(aliClient);
        return aliClient;
    }

}
