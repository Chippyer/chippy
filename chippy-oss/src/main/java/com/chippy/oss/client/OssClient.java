package com.chippy.oss.client;

import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;

/**
 * oss客户端
 *
 * @author: chippy
 **/
public interface OssClient {

    String getClientName();

    UploadResult upload(OssRequestContext ossRequestContext);

}