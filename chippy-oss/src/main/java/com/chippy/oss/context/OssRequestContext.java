package com.chippy.oss.context;

import com.chippy.oss.common.UploadType;

import java.io.InputStream;

/**
 * oss请求上下文
 *
 * @author: chippy
 **/
public interface OssRequestContext {

    /**
     * 客户端名称
     *
     * @return 客户端名称
     * @author chippy
     */
    String getClientName();

    /**
     * 设置客户端名称
     *
     * @param clientName 客户端名称
     * @author chippy
     */
    void setClientName(String clientName);

    /**
     * 文件名称
     *
     * @return 文件名称
     * @author chippy
     */
    String getFileName();

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     * @author chippy
     */
    void setFileName(String fileName);

    /**
     * 获取上传方式
     *
     * @return 上传方式
     * @author chippy
     */
    UploadType getUploadType();

    /**
     * 设置上传方式
     *
     * @param uploadType 上传方式
     * @author chippy
     */
    void setUploadType(UploadType uploadType);

    /**
     * 存储文件文件夹
     *
     * @return 客户端名称
     * @author chippy
     */
    String getFileDir();

    /**
     * 设置存储文件文件夹
     *
     * @param fileDir 文件目录
     * @author chippy
     */
    void setFileDir(String fileDir);

    /**
     * 存储文件分层层级名称
     *
     * @return 客户端名称
     * @author chippy
     */
    String getLevel();

    /**
     * 设置存储文件分层层级名称
     *
     * @param level 分层层级名称
     * @author chippy
     */
    void setLevel(String level);

    /**
     * 文件大小
     *
     * @return 客户端名称
     * @author chippy
     */
    Long getFileSize();

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小
     * @author chippy
     */
    void setFileSize(Long fileSize);

    /**
     * 文件类型
     *
     * @return 客户端名称
     * @author chippy
     */
    String getFileType();

    /**
     * 设置文件类型
     *
     * @param fileType 文件类型
     * @author chippy
     */
    void setFileType(String fileType);

    /**
     * 获取待上传文件流
     *
     * @return 待上传文件流
     * @author chippy
     */
    InputStream getFileStream();

    /**
     * 设置上传文件流
     *
     * @param inputStream 文件流
     * @author chippy
     */
    void setFileStream(InputStream inputStream);

    /**
     * 获取文件字节信息
     *
     * @return 文件字节信息
     * @author chippy
     */
    byte[] getFileBytes();

    /**
     * 设置文件字节信息
     *
     * @param bytes 文件字节信息
     * @author chippy
     */
    void setFileBytes(byte[] bytes);

}