package com.chippy.oss.configuration.core;

import com.chippy.oss.common.UploadType;
import lombok.Data;

/**
 * oss核心上下文属性配置
 *
 * @author: chippy
 **/
@ConfigurationProperties(prefix = "oss.context")
@Data
public class OssContextProperties {

    private String clientName = "DEFAULT-CLIENT";

    private String level;

    private String fileDir = "/";

    private UploadType uploadType = UploadType.NORMAL_IMAGE;

}