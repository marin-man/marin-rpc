package com.manman.rpc.client.handler;

import com.manman.rpc.client.cache.LocalRpcResponseCache;
import com.manman.rpc.core.common.RpcResponse;
import com.manman.rpc.core.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: RpcResponseHandler
 * @Author manman
 * @Description 数据响应处理
 * @Date 2021/11/2
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> messageProtocol) throws Exception {
        String requestId = messageProtocol.getHeader().getRequestId();
        // 收到响应数据，设置响应数据
        LocalRpcResponseCache.fillResponse(requestId, messageProtocol);
    }
}
