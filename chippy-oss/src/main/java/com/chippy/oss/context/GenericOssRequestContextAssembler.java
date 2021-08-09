package com.chippy.oss.context;

import com.chippy.common.utils.ObjectsUtil;
import com.chippy.oss.common.UploadType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * oss请求上下文转换器
 *
 * @author: chippy
 **/
@Slf4j
public class GenericOssRequestContextAssembler implements OssRequestContextAssembler {

    private static final String PARSE_IO_EXCEPTION = "解析文件流异常-[{}]";
    private static final String UPLOAD_FILE_IS_EMPTY = "上传文件参数为空";

    @Override
    public OssRequestContext assembler(MultipartFile multipartFile, String defaultClientName, String level,
        String fileDir, UploadType uploadType) throws IOException {
        if (ObjectsUtil.isEmpty(multipartFile)) {
            throw new IllegalArgumentException(UPLOAD_FILE_IS_EMPTY);
        }

        final OssRequestContext ossRequestContext = new GenericOssRequestContext();
        ossRequestContext.setClientName(defaultClientName);
        ossRequestContext.setLevel(level);
        ossRequestContext.setFileDir(fileDir);
        ossRequestContext.setUploadType(uploadType);

        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(PARSE_IO_EXCEPTION, e.getMessage(), e);
            }
            throw e;
        }
        final String originalFilename = multipartFile.getOriginalFilename();
        ossRequestContext.setFileName(originalFilename);
        ossRequestContext.setFileSize(multipartFile.getSize());
        ossRequestContext.setFileStream(inputStream);
        ossRequestContext.setFileType(this.getFileType(originalFilename));
        ossRequestContext.setFileBytes(multipartFile.getBytes());
        return ossRequestContext;
    }

    private String getFileType(String originalFileName) {
        final String[] originalFileNameArr = originalFileName.split("\\.");
        return originalFileNameArr[originalFileNameArr.length - 1];
    }

}