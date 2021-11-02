package com.manman.rpc.client.proxy;

import com.manman.rpc.client.config.RpcClientProperties;
import com.manman.rpc.client.transport.NetClientTransportFactory;
import com.manman.rpc.client.transport.RequestMetaData;
import com.manman.rpc.core.common.RpcRequest;
import com.manman.rpc.core.common.RpcResponse;
import com.manman.rpc.core.common.ServiceInfo;
import com.manman.rpc.core.common.ServiceUtil;
import com.manman.rpc.core.discovery.DiscoveryService;
import com.manman.rpc.core.exception.ResourceNotFoundException;
import com.manman.rpc.core.exception.RpcException;
import com.manman.rpc.core.protocol.MessageHeader;
import com.manman.rpc.core.protocol.MessageProtocol;
import com.manman.rpc.core.protocol.MsgStatus;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Title: ClientStubInvocationHandler
 * @Author manman
 * @Description 代理实现
 * @Date 2021/11/2
 */
@Slf4j
public class ClientStubInvocationHandler implements InvocationHandler {

    private DiscoveryService discoveryService;

    private RpcClientProperties properties;

    private Class<?> clazz;

    private String version;

    public ClientStubInvocationHandler(DiscoveryService discoveryService, RpcClientProperties properties, Class<?> clazz, String version) {
        super();
        this.clazz = clazz;
        this.version = version;
        this.discoveryService = discoveryService;
        this.properties = properties;
    }

    /**
     * 动态代理实现
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 获取服务信息
        ServiceInfo serviceInfo = discoveryService.discovery(ServiceUtil.serviceKey(this.clazz.getName(), this.version));
        if (serviceInfo == null) {
            throw new ResourceNotFoundException("404");
        }

        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
        // 设置请求头
        messageProtocol.setHeader(MessageHeader.build(properties.getSerialization()));
        // 设置请求体
        RpcRequest request = new RpcRequest();
        request.setServiceName(ServiceUtil.serviceKey(this.clazz.getName(), this.version));
        request.setMethod(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        messageProtocol.setBody(request);

        // 发送网络请求，拿到结果
        MessageProtocol<RpcResponse> responseMessageProtocol = NetClientTransportFactory.getNetClientTransport()
                .sendRequest(RequestMetaData.builder().protocol(messageProtocol).address(serviceInfo.getAddress())
                .port(serviceInfo.getPort()).timeout(properties.getTimeout()).build());

        if (responseMessageProtocol == null) {
            log.error("请求超时");
            throw new RpcException("rpc 调用结果失败，请求超时 timeout:" + properties.getTimeout());
        }
        if (!MsgStatus.isSuccess(responseMessageProtocol.getHeader().getStatus())) {
            log.error("rpc 调用结果失败，message {}", responseMessageProtocol.getBody().getMessage());
            throw new RpcException(responseMessageProtocol.getBody().getMessage());
        }
        return responseMessageProtocol.getBody().getData();
    }
}
