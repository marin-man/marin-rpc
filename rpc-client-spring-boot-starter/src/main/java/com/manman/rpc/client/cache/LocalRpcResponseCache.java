package com.manman.rpc.client.cache;

import com.manman.rpc.client.transport.RpcFuture;
import com.manman.rpc.core.common.RpcResponse;
import com.manman.rpc.core.protocol.MessageProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title: LocalRpcResponseCache
 * @Author manman
 * @Description 请求和响应映射对象
 * @Date 2021/11/2
 */
public class LocalRpcResponseCache {

    private static Map<String, RpcFuture<MessageProtocol<RpcResponse>>> requestResponseCache = new ConcurrentHashMap<>();

    /**
     * 添加请求和响应的映射关系
     * @param reqId
     * @param future
     */
    public static void add(String reqId, RpcFuture<MessageProtocol<RpcResponse>> future) {
        requestResponseCache.put(reqId, future);
    }

    public static void fillResponse(String reqId, MessageProtocol<RpcResponse> messageProtocol) {
        // 获取缓存中的 future
        RpcFuture<MessageProtocol<RpcResponse>> future = requestResponseCache.get(reqId);
        // 设置数据
        future.setResponse(messageProtocol);
        // 移除缓存
        requestResponseCache.remove(reqId);
    }
}
