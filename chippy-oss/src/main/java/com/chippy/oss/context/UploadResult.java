package com.chippy.oss.context;

import lombok.Data;

import java.io.Serializable;

/**
 * 上传结果实体
 *
 * @author: chippy
 */
@Data
public class UploadResult implements Serializable {

    /**
     * 文件地址
     */
    private String url;

    public UploadResult(String url) {
        this.url = url;
    }

}
