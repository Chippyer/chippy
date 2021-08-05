package com.chippy.oss.context;

import com.chippy.oss.client.OssClient;
import com.chippy.oss.predicate.OssPredicateHandler;
import com.chippy.spring.common.factory.DelegateBeanFactory;

import javax.annotation.Resource;

/**
 * 上传客户端模板
 *
 * @author: chippy
 */
@Resource
public class OssClientTemplate {

    @Resource
    private DelegateBeanFactory delegateBeanFactory;

    @Resource
    private OssPredicateHandler ossPredicateHandler;

    public UploadResult upload(OssRequestContext ossRequestContext) {
        ossPredicateHandler.preHandler(ossRequestContext);
        final OssClient ossClient = this.getOssClient(ossRequestContext);
        final UploadResult uploadResult = ossClient.upload(ossRequestContext);
        ossPredicateHandler.postHandler(ossRequestContext, uploadResult);
        return uploadResult;
    }

    private OssClient getOssClient(OssRequestContext ossRequestContext) {
        return delegateBeanFactory.getBean(ossRequestContext.getClientType(), OssClient.class);
    }

}
