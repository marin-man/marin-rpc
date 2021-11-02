package com.manman.rpc.server.store;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: LocalServerCache
 * @Author manman
 * @Description 将暴露的服务缓存到本地
 * 在处理 RPC 请求时可以直接通过 cache 拿到对应的服务进行调用，避免反射实例化服务开销
 * @Date 2021/11/2
 */
public class LocalServerCache {

    private static final Map<String, Object> serverCacheMap = new HashMap<>();

    public static void store(String serverName, Object server) {
        serverCacheMap.merge(serverName, server, (Object oldObj, Object newObj) -> newObj);
    }

    public static Object get(String serverName) {
        return serverCacheMap.get(serverName);
    }

    public static Map<String, Object> getAll() {
        return null;
    }
}
