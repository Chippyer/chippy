package com.chippy.oss.predicate;

import com.chippy.oss.configuration.OssProperties;

import javax.annotation.Resource;

/**
 * 抽象通用限定属性定义
 *
 * @author: chippy
 */
public abstract class AbstractPredicate implements OssPredicate {

    @Resource
    protected OssProperties ossProperties;

}
