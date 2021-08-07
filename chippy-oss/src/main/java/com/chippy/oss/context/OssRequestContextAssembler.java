package com.chippy.oss.context;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * oss请求上下文转换器
 *
 * @author: chippy
 **/
public interface OssRequestContextAssembler {

    /**
     * 将MultipartFile实例转换为oss请求上下文参数
     *
     * @param multipartFile 文件
     * @return oss请求上下文
     * @author chippy
     */
    OssRequestContext assembler(MultipartFile multipartFile) throws IOException;

}