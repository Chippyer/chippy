package com.chippy.oss.client;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.chippy.oss.common.InnerOssClientType;
import com.chippy.oss.common.UploadType;
import com.chippy.oss.configuration.client.AliClientProperties;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云文件上传客户端
 *
 * @author: chippy
 */
@Slf4j
public class AliClient implements OssClient {

    private static final String NO_CACHE = "no-cache";
    private static final String PRAGMA = "Pragma";
    private static final String INLINE_FILENAME = "inline;filename=%s";
    private static final String IO_EXCEPTION = "上传文件时IO异常-[fileName:%s]";
    private static final String HTTP_PRE = "http://";
    private static final String HTTPS_PRE = "https://";

    private OSS client;
    private AliClientProperties aliClientProperties;

    public AliClient(OSS client, AliClientProperties aliClientProperties) {
        this.client = client;
        this.aliClientProperties = aliClientProperties;
    }

    @Override
    public String getClientName() {
        return InnerOssClientType.ALI.name();
    }

    @Override
    public UploadType getDefaultUploadType() {
        return UploadType.NORMAL_IMAGE;
    }

    @Override
    public String getDefaultFileDir() {
        return null;
    }

    @Override
    public UploadResult upload(OssRequestContext ossRequestContext) {
        final String bucketName = ossRequestContext.getLevel();
        final String fileName = ossRequestContext.getFileName();
        final String fileDir = ossRequestContext.getFileDir();
        final String uploadName = fileDir + fileName;

        try (InputStream fileStream = ossRequestContext.getFileStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileStream.available());
            objectMetadata.setCacheControl(NO_CACHE);
            objectMetadata.setHeader(PRAGMA, NO_CACHE);
            objectMetadata.setContentDisposition(String.format(INLINE_FILENAME, fileName));
            client.putObject(bucketName, uploadName, fileStream, objectMetadata);

            final String url =
                HTTPS_PRE + bucketName + '.' + aliClientProperties.getEndpoint() + '/' + fileDir + uploadName;
            return new UploadResult(url);
        } catch (IOException e) {
            throw new ClientException(String.format(IO_EXCEPTION, fileName), e);
        }
    }

    @Override
    public void destroy() {
        this.client.shutdown();
    }

}
