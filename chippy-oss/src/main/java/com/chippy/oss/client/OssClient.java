package com.chippy.oss.client;

import com.chippy.oss.context.OssRequestContext;
import com.chippy.oss.context.UploadResult;
import com.chippy.spring.common.factory.Type;

/**
 * oss客户端
 *
 * @author: chippy
 **/
public interface OssClient extends Type {

    UploadResult upload(OssRequestContext ossRequestContext);

}