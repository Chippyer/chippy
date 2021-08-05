package com.chippy.oss.client;

import com.aliyun.oss.OSSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 阿里云oss客户端配置类
 *
 * @author: chippy
 */
@Configuration
public class AliClientConfiguration {

    @Resource
    private AliClientProperties aliClientProperties;

    @Bean
    public OSSClient ossClient() {
        return new OSSClient(aliClientProperties.getEndpoint(), aliClientProperties.getAccessId(),
            aliClientProperties.getAccessKey());
    }

}
