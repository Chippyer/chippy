package com.chippy.oss.configuration.predicate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * oss属性配置
 *
 * @author: chippy
 */
@ConfigurationProperties(prefix = "oss.predicate")
@Data
public class OssPredicateProperties {

    private long maxFileSize;

    private List<TypePredicateProperties> type;

    @Data
    public static class TypePredicateProperties implements Serializable {

        private String type;

        private long maxSize;

    }

}
