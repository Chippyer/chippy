package com.chippy.oss.predicate;

import com.chippy.oss.configuration.predicate.OssPredicateProperties;

/**
 * 抽象通用限定属性定义
 *
 * @author: chippy
 */
public abstract class AbstractPredicate implements OssPredicate {

    protected OssPredicateProperties ossProperties;

    public AbstractPredicate(OssPredicateProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

}
