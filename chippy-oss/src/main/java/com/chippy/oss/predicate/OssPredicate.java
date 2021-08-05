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
     * @date 2021-08-05 19:57
     */
    String getErrorMsg();

}