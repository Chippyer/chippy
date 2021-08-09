package com.chippy.oss.context;

/**
 * oss处理类
 *
 * @author: chippy
 **/
public interface OssHandler {

    default void preHandler(OssRequestContext ossRequestContext) {
    }

    default void postHandler(OssRequestContext ossRequestContext, UploadResult uploadResult) {
    }

}