package com.chippy.oss.context;

import lombok.Builder;
import lombok.Data;

/**
 * 快照文件结果实体
 *
 * @author: chippy
 **/
@Data
@Builder
public class SnapshotUploadResult {

    private String url;

    private String onlyFileMark;

}