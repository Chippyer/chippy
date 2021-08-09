package com.chippy.oss.configuration.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * oss快照数据配置
 *
 * @author: chippy
 **/
@ConfigurationProperties(prefix = "oss.snapshot")
@Data
public class OssSnapshotProperties {

    private int maxSize;

}