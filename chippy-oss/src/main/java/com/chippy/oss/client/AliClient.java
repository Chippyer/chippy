package com.chippy.oss.client;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.chippy.oss.common.OssClientType;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * 阿里云文件上传客户端
 *
 * @author: chippy
 */
@Slf4j
public class AliClient implements OssClient {

    public static final String NO_CACHE = "no-cache";
    public static final String PRAGMA = "Pragma";
    public static final String INLINE_FILENAME = "inline;filename=%s";
    public static final String IO_EXCEPTION = "上传文件时IO异常-[fileName:%s]";
    public static final String HTTP_PRE = "http://";

    @Resource
    private OSSClient client;

    @Resource
    private AliClientProperties aliClientProperties;

    @Override
    public UploadResult upload(OssRequestContext ossRequestContext) {
        final String bucketName = ossRequestContext.getLevelName();
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
                HTTP_PRE + bucketName + '.' + aliClientProperties.getEndpoint() + '/' + fileDir + uploadName;
            return new UploadResult(url);
        } catch (IOException e) {
            throw new ClientException(String.format(IO_EXCEPTION, fileName), e);
        }
    }

    @Override
    public List<String> getSupportTypeList() {
        return Collections.singletonList(OssClientType.ALI.name());
    }

}
