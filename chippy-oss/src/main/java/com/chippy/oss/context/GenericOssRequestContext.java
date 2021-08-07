package com.chippy.oss.context;

import com.chippy.oss.common.UploadType;
import lombok.Data;

import java.io.InputStream;

/**
 * 通用oss请求上下文
 *
 * @author: chippy
 **/
@Data
public class GenericOssRequestContext implements OssRequestContext {

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 获取上传方式
     */
    private UploadType uploadType;

    /**
     * 存储文件文件夹
     */
    private String fileDir;

    /**
     * 存储文件分层层级名称
     */
    private String level;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 获取待上传文件流
     */
    private InputStream fileStream;

}