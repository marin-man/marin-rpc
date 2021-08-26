package com.manman.rpc.handler;

import com.manman.rpc.message.PingMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: PingMessageHandler
 * @Author manman
 * @Description 心跳消息接收器
 * @Date 2021/8/26
 */
@Slf4j
@ChannelHandler.Sharable
public class PingMessageHandler extends SimpleChannelInboundHandler<PingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PingMessage msg) throws Exception {
        log.debug("接收到心跳信号{}", msg.getMessageType());
    }
}
