package com.chippy.oss.client;

import com.chippy.oss.common.UploadType;
import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import org.springframework.beans.factory.DisposableBean;

/**
 * oss客户端
 *
 * @author: chippy
 **/
public interface OssClient extends DisposableBean {

    /**
     * 获取客户端名称
     *
     * @return 客户端名称
     * @author chippy
     */
    String getClientName();

    /**
     * 获取默认上传文件方式
     *
     * @return 上传文件方式
     * @author chippy
     */
    UploadType getDefaultUploadType();

    /**
     * 获取默认上传文件目录
     *
     * @return 上传文件目录
     * @author chippy
     */
    String getDefaultFileDir();

    /**
     * 上传文件信息
     *
     * @param ossRequestContext 请求上下文内容
     * @return 上传文件的结果信息
     * @author chippy
     */
    UploadResult upload(OssRequestContext ossRequestContext);

}