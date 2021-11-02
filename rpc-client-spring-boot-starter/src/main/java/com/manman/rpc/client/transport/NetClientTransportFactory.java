package com.manman.rpc.client.transport;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: NetClinetTransportFactory
 * @Author manman
 * @Description 网络请求服务工厂
 * @Date 2021/11/2
 */
@Slf4j
public class NetClientTransportFactory {

    public static NettyNetClientTransport getNetClientTransport() {
        return new NettyNetClientTransport();
    }
}
