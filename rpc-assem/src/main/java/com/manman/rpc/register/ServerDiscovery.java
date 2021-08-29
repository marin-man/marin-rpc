package com.manman.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

/**
 * @Title: ServerDiscovery
 * @Author manman
 * @Description 服务发现
 * @Date 2021/8/29
 */
public interface ServerDiscovery {

    /**
     * 根据服务名找到 InetSocketAddress
     * @param serviceName
     * @return
     */
    InetSocketAddress getService(String serviceName) throws NacosException;
}
