package com.chippy.oss.context;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import com.chippy.oss.configuration.core.OssSnapshotProperties;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * oss lru算法快照实现
 *
 * @author: chippy
 **/
public class OssLruSnapshot implements OssSnapshot {

    private static final int DEFAULT_CAPACITY = 1024 * 8;
    private static final int ZERO = 0;
    private LRUCache<String, SnapshotUploadResult> snapshotUploadResult;

    public OssLruSnapshot() {
        this(DEFAULT_CAPACITY);
    }

    public OssLruSnapshot(OssSnapshotProperties ossSnapshotProperties) {
        this(ossSnapshotProperties.getMaxSize());
    }

    public OssLruSnapshot(int capacity) {
        this.loadSnapshotCapacity(capacity);
    }

    @Override
    public SnapshotUploadResult get(byte[] bytes) {
        return this.get(this.getOnlyFileMark(bytes));
    }

    @Override
    public SnapshotUploadResult get(String onlyFileMark) {
        return this.snapshotUploadResult.get(onlyFileMark);
    }

    @Override
    public void put(SnapshotUploadResult snapshotUploadResult) {
        this.snapshotUploadResult.put(snapshotUploadResult.getOnlyFileMark(), snapshotUploadResult);
    }

    @Override
    public void put(List<SnapshotUploadResult> snapshotUploadResultList) {
        for (SnapshotUploadResult snapshotUploadResult : snapshotUploadResultList) {
            this.put(snapshotUploadResult);
        }
    }

    @Override
    public void remove(String onlyFileMark) {
        snapshotUploadResult.remove(onlyFileMark);
    }

    @Override
    public void remove(List<String> onlyFileMarkList) {
        for (String onlyFileMark : onlyFileMarkList) {
            this.remove(onlyFileMark);
        }
    }

    @Override
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