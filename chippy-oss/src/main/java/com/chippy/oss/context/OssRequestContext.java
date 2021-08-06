package com.chippy.oss.context;

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
     * 文件名称
     *
     * @return 文件名称
     * @author chippy
     */
    String getFileName();

    /**
     * 存储文件文件夹
     *
     * @return 客户端名称
     * @author chippy
     */
    String getFileDir();

    /**
     * 存储文件分层层级名称
     *
     * @return 客户端名称
     * @author chippy
     */
    String getLevelName();

    /**
     * 文件大小
     *
     * @return 客户端名称
     * @author chippy
     */
    Long getFileSize();

    /**
     * 文件类型
     *
     * @return 客户端名称
     * @author chippy
     */
    String getFileType();

    /**
     * 获取待上传文件流
     *
     * @return 待上传文件流
     * @author chippy
     */
    InputStream getFileStream();

}