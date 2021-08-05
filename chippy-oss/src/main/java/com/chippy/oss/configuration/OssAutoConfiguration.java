package com.chippy.oss.configuration;

import com.chippy.oss.client.AliClient;
import com.chippy.oss.client.AliClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * oss自动配置类
 *
 * @author: chippy
 */
@Configuration
@Import(AliClientConfiguration.class)
public class OssAutoConfiguration {

    @Bean
    public AliClient aliClient() {
        return new AliClient();
    }

}
