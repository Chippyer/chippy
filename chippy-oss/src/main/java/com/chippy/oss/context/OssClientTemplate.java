package com.chippy.oss.context;

import com.chippy.oss.client.OssClient;
import com.chippy.oss.configuration.client.OssClientContext;
import com.chippy.oss.predicate.OssPredicateHandler;

import javax.annotation.Resource;

/**
 * 上传客户端模板
 *
 * @author: chippy
 */
public class OssClientTemplate {

    @Resource
    private OssPredicateHandler ossPredicateHandler;

    @Resource
    private OssClientContext ossClientContext;

    public UploadResult upload(OssRequestContext ossRequestContext) {
        ossPredicateHandler.preHandler(ossRequestContext);
        final OssClient ossClient = ossClientContext.getOssClient(ossRequestContext.getClientName());
        final UploadResult uploadResult = ossClient.upload(ossRequestContext);
        ossPredicateHandler.postHandler(ossRequestContext, uploadResult);
        return uploadResult;
    }

}
