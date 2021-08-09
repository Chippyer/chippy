package com.chippy.oss.context;

import java.util.List;

/**
 * oss快照操作
 *
 * @author: chippy
 **/
public interface OssSnapshot {

    SnapshotUploadResult get(byte[] bytes);

    SnapshotUploadResult get(String onlyFileMark);

    void put(SnapshotUploadResult snapshotUploadResult);

    void put(List<SnapshotUploadResult> snapshotUploadResultList);

    void remove(String onlyFileMark);

    void remove(List<String> onlyFileMarkList);

    String getOnlyFileMark(byte[] bytes);

}