package com.chippy.oss.predicate;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.configuration.predicate.OssPredicateProperties;
import com.chippy.oss.context.OssRequestContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件大小限制
 *
 * @author: chippy
 **/
public class FileSizePredicate extends AbstractPredicate {

    private Long maxSize;

    public FileSizePredicate(OssPredicateProperties ossProperties) {
        super(ossProperties);
        this.maxSize = ossProperties.getMaxFileSize();
    }

    @Override
    public boolean test(OssRequestContext ossRequestContext) {
        final OssPredicateProperties.TypePredicateProperties fileTypePredicateProperties =
            this.getFileTypePredicateProperties(ossRequestContext);
        final boolean isEmpty = ObjectsUtil.isEmpty(fileTypePredicateProperties);
        final boolean isInvalid = Objects.equals(fileTypePredicateProperties.getMaxSize(), 0L);
        if (isEmpty || isInvalid) {
            return ossRequestContext.getFileSize() < maxSize;
        }
        final Long finalMaxSize = this.getMaxSize(fileTypePredicateProperties);
        return ossRequestContext.getFileSize() < finalMaxSize;
    }

    @Override
    public String getErrorMsg() {
        return String.format("上传文件最大-[%s]", ossProperties.getMaxFileSize());
    }

    private Long getMaxSize(OssPredicateProperties.TypePredicateProperties fileTypePredicateProperties) {
        final long maxFileSize = ossProperties.getMaxFileSize();
        final long maxSize = fileTypePredicateProperties.getMaxSize();
        return Math.min(maxSize, maxFileSize);
    }

    private OssPredicateProperties.TypePredicateProperties getFileTypePredicateProperties(
        OssRequestContext ossRequestContext) {
        final List<OssPredicateProperties.TypePredicateProperties> typePredicatePropertiesList =
            ossProperties.getType();
        final List<OssPredicateProperties.TypePredicateProperties> filteredResult = typePredicatePropertiesList.stream()
            .filter(typePredicateProperties -> ObjectsUtil
                .eqIgnoreAttach(typePredicateProperties.getType(), ossRequestContext.getFileType()))
            .collect(Collectors.toList());
        return ObjectsUtil.isNotEmpty(filteredResult) ? filteredResult.get(0) : null;
    }

}