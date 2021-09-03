package com.manman.rpc.api.service;

import com.manman.rpc.annotation.RpcServer;

/**
 * @Title: HelloServiceImpl
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
@RpcServer
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好，" + name;
    }
}
