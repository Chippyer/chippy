package com.chippy.oss.context;

import com.chippy.oss.common.UploadType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * oss客户端模板
 *
 * @author: chippy
 **/
public interface OssClientTemplate {

    UploadResult upload(MultipartFile file, String clientName, String level) throws IOException;

    UploadResult upload(MultipartFile file, String clientName, String level, String fileDir) throws IOException;

    UploadResult upload(MultipartFile file, String clientName, String level, UploadType uploadType) throws IOException;

    UploadResult upload(MultipartFile file, String clientName, String level, String fileDir, UploadType uploadType)
        throws IOException;

}