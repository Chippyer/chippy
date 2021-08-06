package com.chippy.oss.predicate;

import com.chippy.oss.context.OssRequestContext;

import java.util.function.Predicate;

/**
 * oss断言器
 *
 * @author: chippy
 **/
public interface OssPredicate extends Predicate<OssRequestContext> {

    /**
     * 获取执行异常信息
     *
     * @return java.lang.String
     * @author chippy
     */
    String getErrorMsg();

}