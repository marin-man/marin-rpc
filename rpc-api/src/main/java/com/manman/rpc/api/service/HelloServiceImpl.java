package com.manman.rpc.api.service;

/**
 * @Title: HelloServiceImpl
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好，" + name;
    }
}
