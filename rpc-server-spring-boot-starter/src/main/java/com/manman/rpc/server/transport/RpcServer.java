package com.manman.rpc.server.transport;

/**
 * @Title: RpcServer
 * @Author manman
 * @Description rpc 服务管理
 * @Date 2021/11/2
 */
public interface RpcServer {

    /**
     * 开启服务
     * @param port
     */
    void start(int port);
}
