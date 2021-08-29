package com.manman.rpc.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: HeartBeatServerHandler
 * @Author manman
 * @Description 服务端心跳检测处理器
 * @Date 2021/8/26
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelDuplexHandler {

    /**
     * 处理心跳事件 IdleStateEvent
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 读空闲事件
            if (event.state() == IdleState.READER_IDLE) {
                log.debug("长时间没有收到消息了，断开连接");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
