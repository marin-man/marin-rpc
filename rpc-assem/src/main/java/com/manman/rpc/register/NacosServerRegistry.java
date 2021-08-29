package com.manman.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Title: NacosServerRegistry
 * @Author manman
 * @Description nacos 服务注册
 * @Date 2021/8/29
 */
@Slf4j
public class NacosServerRegistry implements ServerRegistry {

    /**
     * 服务注册
     * @param serviceName
     * @param inetSocketAddress
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtils.registerServer(serviceName, inetSocketAddress);
            log.debug("注册{}", serviceName);
        } catch (NacosException e) {
            throw new RuntimeException("注册 Nacos 出现异常");
        }
    }
}