package com.chippy.oss.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * oss属性配置
 *
 * @author: chippy
 */
@EnableConfigurationProperties({OssProperties.class})
@ConfigurationProperties(prefix = "oss.predicate")
@Data
public class OssProperties {

    private Long maxFileSize;

    private List<TypePredicateProperties> typePredicatePropertiesList;

    @Data
    public static class TypePredicateProperties implements Serializable {

        private String type;

        private Long maxSize;

    }

}
