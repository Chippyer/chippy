package com.chippy.oss.context;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.client.OssClient;
import com.chippy.oss.common.UploadType;
import com.chippy.oss.exception.OssClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 上传客户端模板
 *
 * @author: chippy
 */
public class GenericOssClientTemplate implements OssClientTemplate {

    private static final String OSS_CLIENT_IS_EMPTY = "未获取到上传客户端";

    private List<OssHandler> ossHandlerList;
    private OssClientContext ossClientContext;
    private GenericOssRequestContextAssembler genericOssRequestContextAssembler;

    public GenericOssClientTemplate(List<OssHandler> ossHandlerList, OssClientContext ossClientContext,
        GenericOssRequestContextAssembler genericOssRequestContextAssembler) {
        this.ossHandlerList = ossHandlerList;
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
        final SnapshotUploadResult snapshotUploadResult =
            this.getSnapshotUploadResult(ossRequestContext.getFileBytes());
        if (ObjectsUtil.isNotEmpty(snapshotUploadResult)) {
            return new UploadResult(snapshotUploadResult.getUrl());
        }
        this.doPreHandler(ossRequestContext);
        final OssClient ossClient = this.getOssClient(ossRequestContext.getClientName());
        final UploadResult uploadResult = ossClient.upload(ossRequestContext);
        this.doAfterHandler(ossRequestContext, uploadResult);
        return uploadResult;
    }

    private SnapshotUploadResult getSnapshotUploadResult(byte[] bytes) {
        final OssSnapshotHandler ossSnapshotResultHandler = this.getOssSnapshotResultHandler();
        if (ObjectsUtil.isEmpty(ossSnapshotResultHandler)) {
            return null;
        }
        return ossSnapshotResultHandler.get(bytes);
    }

    private OssSnapshotHandler getOssSnapshotResultHandler() {
        for (OssHandler ossHandler : ossHandlerList) {
            if (ossHandler instanceof OssSnapshotHandler) {
                return (OssSnapshotHandler)ossHandler;
            }
        }
        return null;
    }

    private void doPreHandler(OssRequestContext ossRequestContext) {
        for (OssHandler ossHandler : ossHandlerList) {
            ossHandler.preHandler(ossRequestContext);
        }
    }

    private void doAfterHandler(OssRequestContext ossRequestContext, UploadResult uploadResult) {
        for (OssHandler ossHandler : ossHandlerList) {
            ossHandler.postHandler(ossRequestContext, uploadResult);
        }
    }

    private OssClient getOssClient(String clientName) {
        final OssClient ossClient = ossClientContext.getOssClient(clientName);
        if (ObjectsUtil.isEmpty(ossClient)) {
            throw new OssClientException(OSS_CLIENT_IS_EMPTY);
        }
        return ossClient;
    }

}
