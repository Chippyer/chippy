package com.chippy.oss.context;

import com.chippy.oss.client.OssClient;

import java.util.HashMap;
import java.util.Map;

/**
 * oss客户端配置上下文
 *
 * @author: chippy
 */
public class OssClientContext {

    private Map<String/*clientName*/, OssClient> ossClientMap = new HashMap<>();

    public void register(OssClient ossClient) {
        ossClientMap.put(ossClient.getClientName(), ossClient);
    }

    public OssClient getOssClient(String clientName) {
        return ossClientMap.get(clientName);
    }

}
