package com.manman.rpc.proxy;

import com.manman.rpc.manager.RpcClientManager;
import com.manman.rpc.message.RpcRequestMessage;
import com.manman.rpc.protocol.SequenceIdGenerator;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.Proxy;

/**
 * @Title: ClientProxy
 * @Author manman
 * @Description 客户端创建代理类
 * @Date 2021/8/29
 */
public class ClientProxy {

    private final RpcClientManager RPC_CLIENT;


    public ClientProxy(RpcClientManager client) {
        RPC_CLIENT = client;
    }

    /**
     * JDK 动态代理创建代理类
     * @param serviceClass
     * @param <T>
     * @return
     */
    public <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = serviceClass.getInterfaces();
        // 创建代理对象
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 1. 将此方法调用转换为消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            // 2. 准备一个空 Promise 对象，来接收结果，存入集合
            DefaultPromise<Object> promise = new DefaultPromise<>(RpcClientManager.group.next());
            RpcClientManager.PROMISES.put(sequenceId, promise);
            // 3. 将消息对象发送出去
            RPC_CLIENT.sendRpcRequest(msg);
            // 4. 等待 promise 结果
            promise.await();
            if (promise.isSuccess())
                return promise.getNow();  // 调用正常
            else
                throw new RuntimeException(promise.cause().getMessage());
        });
        return (T) o;
    }
}
