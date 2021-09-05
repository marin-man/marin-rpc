package com.manman.rpc;

import com.manman.rpc.annotation.RpcServerScan;
import com.manman.rpc.manager.RpcServiceManager;

/**
 * @Title: RpcServer
 * @Author manman
 * @Description 服务端测试
 * @Date 2021/8/29
 */
@RpcServerScan
public class RpcServer {
    public static void main(String[] args) {
        // 创建服务管理器，启动服务
        new RpcServiceManager().start();
    }
}
