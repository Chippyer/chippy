package com.chippy.oss.configuration.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云oss客户端配置信息
 *
 * @author: chippy
 */
@ConfigurationProperties(prefix = "oss.client.ali")
@Data
public class AliClientProperties {

    private String endpoint;

    private String accessId;

    private String accessKey;

}
