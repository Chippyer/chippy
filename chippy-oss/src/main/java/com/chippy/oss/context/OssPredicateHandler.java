package com.chippy.oss.context;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.exception.OssPredicateException;
import com.chippy.oss.predicate.OssPredicate;

import java.util.List;

/**
 * oss断言器处理类
 *
 * @author: chippy
 **/
public class OssPredicateHandler implements OssHandler {

    private List<OssPredicate> ossPredicateList;

    public OssPredicateHandler(List<OssPredicate> ossPredicateList) {
        this.ossPredicateList = ossPredicateList;
    }

    @Override
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

}