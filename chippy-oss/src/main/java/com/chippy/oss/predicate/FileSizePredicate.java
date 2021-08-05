package com.chippy.oss.predicate;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.configuration.OssProperties;
import com.chippy.oss.context.OssRequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件大小限制
 *
 * @author: chippy
 **/
@Service
public class FileSizePredicate extends AbstractPredicate implements InitializingBean {

    private Long maxSize;

    @Override
    public boolean test(OssRequestContext ossRequestContext) {
        final OssProperties.TypePredicateProperties fileTypePredicateProperties =
            this.getFileTypePredicateProperties(ossRequestContext);
        if (ObjectsUtil.isEmpty(fileTypePredicateProperties)) {
            return ossRequestContext.getFileSize() < maxSize;
        }

        final Long finalMaxSize = this.getMaxSize(fileTypePredicateProperties);
        return ossRequestContext.getFileSize() < finalMaxSize;
    }

    @Override
    public String getErrorMsg() {
        return String.format("上传文件最大-[%s]", ossProperties.getMaxFileSize());
    }

    @Override
    public void afterPropertiesSet() {
        maxSize = ossProperties.getMaxFileSize();
    }

    private Long getMaxSize(OssProperties.TypePredicateProperties fileTypePredicateProperties) {
        final Long maxFileSize = ossProperties.getMaxFileSize();
        final Long maxSize = fileTypePredicateProperties.getMaxSize();
        return Math.min(maxSize, maxFileSize);
    }

    private OssProperties.TypePredicateProperties getFileTypePredicateProperties(OssRequestContext ossRequestContext) {
        final List<OssProperties.TypePredicateProperties> typePredicatePropertiesList =
            ossProperties.getTypePredicatePropertiesList();
        final List<OssProperties.TypePredicateProperties> filteredResult = typePredicatePropertiesList.stream().filter(
            typePredicateProperties -> ObjectsUtil
                .eqIgnoreAttach(typePredicateProperties.getType(), ossRequestContext.getClientType()))
            .collect(Collectors.toList());
        return ObjectsUtil.isNotEmpty(filteredResult) ? filteredResult.get(0) : null;
    }

}