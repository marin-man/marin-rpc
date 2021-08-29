package com.manman.rpc.register;

import java.net.InetSocketAddress;

/**
 * @Title: ServerRegistry
 * @Author manman
 * @Description 服务注册
 * @Date 2021/8/29
 */
public interface ServerRegistry {

    /**
     * 将服务的名称和地址注册到注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
