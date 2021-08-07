package com.chippy.oss.context;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.client.OssClient;
import com.chippy.oss.common.UploadType;
import com.chippy.oss.exception.OssClientException;
import com.chippy.oss.predicate.OssPredicateHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传客户端模板
 *
 * @author: chippy
 */
public class GenericOssClientTemplate implements OssClientTemplate {

    private static final String OSS_CLIENT_IS_EMPTY = "未获取到上传客户端";

    private OssPredicateHandler ossPredicateHandler;
    private OssClientContext ossClientContext;
    private GenericOssRequestContextAssembler genericOssRequestContextAssembler;

    public GenericOssClientTemplate(OssPredicateHandler ossPredicateHandler, OssClientContext ossClientContext,
        GenericOssRequestContextAssembler genericOssRequestContextAssembler) {
        this.ossPredicateHandler = ossPredicateHandler;
        this.ossClientContext = ossClientContext;
        this.genericOssRequestContextAssembler = genericOssRequestContextAssembler;
    }

    @Override
    public UploadResult upload(MultipartFile file, String clientName, String level) throws IOException {
        final OssClient ossClient = this.getOssClient(clientName);
        return this.upload(file, clientName, level, ossClient.getDefaultFileDir(), ossClient.getDefaultUploadType());
    }

    @Override
    public UploadResult upload(MultipartFile file, String clientName, String level, String fileDir) throws IOException {
        final OssClient ossClient = this.getOssClient(clientName);
        return this.upload(file, clientName, level, fileDir, ossClient.getDefaultUploadType());
    }

    @Override
    public UploadResult upload(MultipartFile file, String clientName, String level, UploadType uploadType)
        throws IOException {
        final OssClient ossClient = this.getOssClient(clientName);
        return this.upload(file, clientName, level, ossClient.getDefaultFileDir(), uploadType);
    }

    @Override
    public UploadResult upload(MultipartFile file, String clientName, String level, String fileDir,
        UploadType uploadType) throws IOException {
        final OssRequestContext ossRequestContext =
            genericOssRequestContextAssembler.assembler(file, clientName, level, fileDir, uploadType);
        return this.upload(ossRequestContext);
    }

    private UploadResult upload(OssRequestContext ossRequestContext) {
        ossPredicateHandler.preHandler(ossRequestContext);
        final OssClient ossClient = this.getOssClient(ossRequestContext.getClientName());
        final UploadResult uploadResult = ossClient.upload(ossRequestContext);
        ossPredicateHandler.postHandler(ossRequestContext, uploadResult);
        return uploadResult;
    }

    private OssClient getOssClient(String clientName) {
        final OssClient ossClient = ossClientContext.getOssClient(clientName);
        if (ObjectsUtil.isEmpty(ossClient)) {
            throw new OssClientException(OSS_CLIENT_IS_EMPTY);
        }
        return ossClient;
    }

}
