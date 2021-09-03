package com.manman.rpc.api.service;

import com.manman.rpc.annotation.RpcServer;

/**
 * @Title: NbServiceImpl
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
@RpcServer
public class NbServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "NB ===> " + name;
    }
}
