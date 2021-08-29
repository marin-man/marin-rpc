package com.manman.rpc;

import com.manman.rpc.manager.RpcClientManager;
import com.manman.rpc.proxy.ClientProxy;

/**
 * @Title: RpcClient
 * @Author manman
 * @Description 客户端测试
 * @Date 2021/8/29
 */
public class RpcClient {
    public static void main(String[] args) {
        RpcClientManager clientManager = new RpcClientManager();
        // 创建代理对象
        new ClientProxy(clientManager).getProxyService(LBW)
    }
}
