package com.manman.rpc.api.service;

import com.manman.rpc.annotation.RpcServer;

/**
 * @Title: HelloServer
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
public interface HelloService {
    String sayHello(String name);
}
