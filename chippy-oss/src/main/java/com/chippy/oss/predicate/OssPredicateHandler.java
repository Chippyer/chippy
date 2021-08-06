package com.chippy.oss.predicate;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import com.chippy.oss.exception.OssPredicateException;

import java.util.List;

/**
 * oss断言器处理类
 *
 * @author: chippy
 **/
public class OssPredicateHandler {

    private List<OssPredicate> ossPredicateList;

    public OssPredicateHandler(List<OssPredicate> ossPredicateList) {
        this.ossPredicateList = ossPredicateList;
    }

    public void preHandler(OssRequestContext ossRequestContext) {
        if (ObjectsUtil.isEmpty(ossPredicateList)) {
            return;
        }
        for (OssPredicate ossPredicate : ossPredicateList) {
            if (!ossPredicate.test(ossRequestContext)) {
                throw new OssPredicateException(ossPredicate.getErrorMsg());
            }
        }
    }

    public void postHandler(OssRequestContext ossRequestContext, UploadResult uploadResult) {
    }

}