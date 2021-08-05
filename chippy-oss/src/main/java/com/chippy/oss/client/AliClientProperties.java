package com.chippy.oss.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 阿里云oss客户端配置信息
 *
 * @author: chippy
 */
@EnableConfigurationProperties({AliClientProperties.class})
@ConfigurationProperties(prefix = "oss.client.ali")
@Data
public class AliClientProperties {

    private String endpoint;

    private String accessId;

    private String accessKey;

}
