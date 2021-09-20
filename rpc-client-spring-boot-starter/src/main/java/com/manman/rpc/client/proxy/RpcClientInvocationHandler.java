package com.manman.rpc.client.proxy;

import com.manman.rpc.core.common.ServiceInfo;
import com.manman.rpc.core.common.ServiceUtil;
import com.manman.rpc.core.discovery.RpcServiceDiscovery;
import com.manman.rpc.core.exception.ResourceNotFountException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Title: RpcClientInvocationHandler
 * @Author manman
 * @Description 反射处理类
 * @Date 2021/9/20
 */
public class RpcClientInvocationHandler implements InvocationHandler {

    private RpcServiceDiscovery serverDiscovery;
    private Class<?> clazz;
    private String version;



    public RpcClientInvocationHandler(RpcServiceDiscovery serverDiscovery, Class<?> clazz, String version) {
        super();
        this.serverDiscovery = serverDiscovery;
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 获取服务信息
        ServiceInfo serverInfo = serverDiscovery.discovery(ServiceUtil.serviceKey(this.clazz.getName(), this.version));
        if (serverInfo == null) {
            throw new ResourceNotFountException("404");
        }


    }
}
