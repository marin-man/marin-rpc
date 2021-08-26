package com.manman.rpc.handler;

import com.manman.rpc.manager.RpcClientManager;
import com.manman.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: RpcResponseMessageHandler
 * @Author manman
 * @Description PRC响应处理器
 * @Date 2021/8/26
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        try {
            log.debug("{}", msg);
            // 每次使用完都要移除
            Promise<Object> promise = RpcClientManager.PROMISES.remove(msg.getSequenceId());
            if (promise != null) {
                Object returnValue = msg.getReturnValue();
                Exception exceptionValue = msg.getExceptionValue();
                if (exceptionValue != null) {
                    promise.setFailure(exceptionValue);
                } else {
                    promise.setSuccess(returnValue);
                }
            } else {
                promise.setFailure(new Exception("promise 不存在"));
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("出现异常" + cause);
        ctx.close();
    }
}
