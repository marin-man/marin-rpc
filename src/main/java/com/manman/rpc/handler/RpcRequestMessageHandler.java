package com.manman.rpc.handler;

import com.manman.rpc.factory.ServiceFactory;
import com.manman.rpc.message.RpcRequestMessage;
import com.manman.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Title: RpcRequestMessageHandler
 * @Author manman
 * @Description RPC消息发送处理器
 * @Date 2021/8/26
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) {
        RpcResponseMessage responseMessage = new RpcResponseMessage();
        // 设置请求的序号
        responseMessage.setSequenceId(message.getSequenceId());
        Object result;
        try {
            // 通过名称从工厂获取本地注解了 @RpcServer 的实例
            Object service = ServiceFactory.serviceFactory.get(message.getInterfaceName());
            // 根据方法名、参数，反射获取方法
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            // 通过具体参数信息来调用此方法
            result = method.invoke(service, message.getParameterValue());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("远程调用出错：{}", e.getCause().getMessage());
            responseMessage.setExceptionValue(new Exception("远程调用出错：" + e.getCause().getMessage()));
        }
    }
}
