package com.chippy.oss.context;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import com.chippy.oss.configuration.core.OssSnapshotProperties;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * oss结果处理器
 *
 * @author: chippy
 **/
public class OssSnapshotHandler implements OssHandler {

    private static final int DEFAULT_CAPACITY = 1024 * 8;
    private static final int ZERO = 0;
    private LRUCache<String, SnapshotUploadResult> snapshotUploadResult;

    public OssSnapshotHandler() {
        this(DEFAULT_CAPACITY);
    }

    public OssSnapshotHandler(OssSnapshotProperties ossSnapshotProperties) {
        this(ossSnapshotProperties.getMaxSize());
    }

    public OssSnapshotHandler(int capacity) {
        this.loadSnapshotCapacity(capacity);
    }

    @Override
    public void postHandler(OssRequestContext ossRequestContext, UploadResult uploadResult) {
        final String onlyFileMark = this.getOnlyFileMark(ossRequestContext.getFileBytes());
        final SnapshotUploadResult snapshotUploadResult =
            SnapshotUploadResult.builder().onlyFileMark(onlyFileMark).url(uploadResult.getUrl()).build();
        this.put(snapshotUploadResult);
    }

    public SnapshotUploadResult get(byte[] bytes) {
        return this.snapshotUploadResult.get(this.getOnlyFileMark(bytes));
    }

    public SnapshotUploadResult get(String onlyFileMark) {
        return this.snapshotUploadResult.get(onlyFileMark);
    }

    public void put(SnapshotUploadResult snapshotUploadResult) {
        this.snapshotUploadResult.put(snapshotUploadResult.getOnlyFileMark(), snapshotUploadResult);
    }

    public void put(List<SnapshotUploadResult> snapshotUploadResultList) {
        for (SnapshotUploadResult snapshotUploadResult : snapshotUploadResultList) {
            this.put(snapshotUploadResult);
        }
    }

    public void remove(String onlyFileMark) {
        snapshotUploadResult.remove(onlyFileMark);
    }

    public void remove(List<String> onlyFileMarkList) {
        for (String onlyFileMark : onlyFileMarkList) {
            this.remove(onlyFileMark);
        }
    }

    public String getOnlyFileMark(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    private void loadSnapshotCapacity(int capacity) {
        if (capacity > ZERO) {
            this.snapshotUploadResult = CacheUtil.newLRUCache(capacity);
        } else {
            this.snapshotUploadResult = CacheUtil.newLRUCache(DEFAULT_CAPACITY);
        }
    }

}