package com.manman.rpc.register.zookeeper;

import com.manman.rpc.register.ServerRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Title: ZookeeperRegistry
 * @Author manman
 * @Description zookeeper 注册中心
 * @Date 2021/9/5
 */
@Slf4j
public class ZkServerRegistry implements ServerRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        ZookeeperUtils.registerServer(serviceName, inetSocketAddress);  // 往 zookeeper 中注册服务
        log.debug("注册{}", serviceName);
    }
}
