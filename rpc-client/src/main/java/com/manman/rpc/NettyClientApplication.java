package com.manman.rpc;

import com.manman.rpc.api.service.HelloService;
import com.manman.rpc.api.service.NbServiceImpl;
import com.manman.rpc.manager.RpcClientManager;
import com.manman.rpc.proxy.ClientProxy;

/**
 * @Title: NettyClientApplication
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
public class NettyClientApplication {
    public static void main(String[] args) {
        RpcClientManager clientManager = new RpcClientManager();
        // 创建代理对象
        HelloService service = new ClientProxy(clientManager).getProxyService(NbServiceImpl.class);
        System.out.println(service.sayHello("慢慢"));
    }
}
