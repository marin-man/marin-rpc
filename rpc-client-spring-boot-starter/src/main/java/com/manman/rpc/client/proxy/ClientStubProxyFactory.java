package com.manman.rpc.client.proxy;

import com.manman.rpc.client.config.RpcClientProperties;
import com.manman.rpc.core.discovery.DiscoveryService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: ClientStubProxyFactory
 * @Author manman
 * @Description 代理实现工厂
 * @Date 2021/11/2
 */
public class ClientStubProxyFactory {

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    public <T> T getProxy(Class<T> clazz, String version, DiscoveryService discoveryService, RpcClientProperties properties) {
        return (T) objectCache.computeIfAbsent(clazz, clz ->
            Proxy.newProxyInstance(
                    clz.getClassLoader(),
                    new Class[]{clz},
                    new ClientStubInvocationHandler(discoveryService, properties, clz, version))
            );
    }
}
