package com.chippy.oss.predicate;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import com.chippy.oss.exception.OssPredicateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * oss断言器处理类
 *
 * @author: chippy
 **/
@Service
public class OssPredicateHandler {

    @Resource
    private Map<String /*clientName*/, List<OssPredicate>> ossPredicateMap;

    public void preHandler(OssRequestContext ossRequestContext) {
        final List<OssPredicate> ossPredicateList = ossPredicateMap.get(ossRequestContext.getClientName());
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