package com.manman.rpc.provider.service;

import com.manman.rpc.api.service.HelloWorldService;
import com.manman.rpc.server.annotation.RpcService;

/**
 * @Title: HelloWordServiceImpl
 * @Author manman
 * @Description 测试将服务注册到注册中心
 * @Date 2021/11/2
 */
@RpcService(interfaceType = HelloWorldService.class, version = "1.0")
public class HelloWordServiceImpl implements HelloWorldService {
    @Override
    public String sayHello(String name) {
        return String.format("您好：%s，rpc 调用成功", name);
    }
}
