package com.manman.rpc.api.service;

/**
 * @Title: NbServiceImpl
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
public class NbServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "NB ===> " + name;
    }
}
