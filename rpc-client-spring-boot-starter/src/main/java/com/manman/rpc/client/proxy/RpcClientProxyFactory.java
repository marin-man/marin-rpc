package com.manman.rpc.client.proxy;

import com.manman.rpc.register.ServerDiscovery;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: RpcClientProxyFactory
 * @Author manman
 * @Description 代理对象工厂
 * @Date 2021/9/20
 */
public class RpcClientProxyFactory {

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    public <T> T getProxy(Class<T> clazz, String version, ServerDiscovery serverDiscovery) {
        return (T) objectCache.computeIfAbsent(clazz, clz -> {
            Proxy.newProxyInstance(
                    clz.getClassLoader(),
            )
        })
    }
}
