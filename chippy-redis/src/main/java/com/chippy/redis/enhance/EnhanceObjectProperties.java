package com.chippy.redis.enhance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 增强对象配置属性
 *
 * @author: chippy
 **/
@ConfigurationProperties(prefix = "enhance-object")
@Data
public class EnhanceObjectProperties {

    /**
     * 增强对象缓存容量值
     * 默认16
     */
    private int enhanceObjectCapacity = 16;

    /**
     * 待扫描包路径
     */
    private List<String> scannerPackage;

}