package com.chippy.oss.context;

/**
 * oss结果处理器
 *
 * @author: chippy
 **/
public class OssSnapshotHandler implements OssHandler {

    private OssSnapshot ossSnapshot;

    public OssSnapshotHandler(OssSnapshot ossSnapshot) {
        this.ossSnapshot = ossSnapshot;
    }

    @Override
    public void postHandler(OssRequestContext ossRequestContext, UploadResult uploadResult) {
        this.doHandlerSnapshot(ossRequestContext, uploadResult);
    }

    private void doHandlerSnapshot(OssRequestContext ossRequestContext, UploadResult uploadResult) {
        final String onlyFileMark = ossSnapshot.getOnlyFileMark(ossRequestContext.getFileBytes());
        final SnapshotUploadResult snapshotUploadResult =
            SnapshotUploadResult.builder().onlyFileMark(onlyFileMark).url(uploadResult.getUrl()).build();
        ossSnapshot.put(snapshotUploadResult);
    }

}