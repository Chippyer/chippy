package com.chippy.oss.configuration.predicate;

import com.chippy.oss.configuration.client.OssClientContext;
import com.chippy.oss.predicate.FileSizePredicate;
import com.chippy.oss.predicate.FileTypePredicate;
import com.chippy.oss.predicate.OssPredicate;
import com.chippy.oss.predicate.OssPredicateHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * oss自动配置类
 *
 * @author: chippy
 */
@EnableConfigurationProperties({OssPredicateProperties.class})
@Configuration
public class OssPredicateAutoConfiguration {

    @Resource
    private OssPredicateProperties ossPredicateProperties;

    @Bean
    public OssClientContext ossClientContext() {
        return new OssClientContext();
    }

    @Bean
    public FileSizePredicate fileSizePredicate() {
        return new FileSizePredicate(ossPredicateProperties);
    }

    @Bean
    public FileTypePredicate fileTypePredicate() {
        return new FileTypePredicate(ossPredicateProperties);
    }

    @Bean
    public OssPredicateHandler ossPredicateHandler(List<OssPredicate> ossPredicateList) {
        return new OssPredicateHandler(ossPredicateList);
    }

}
