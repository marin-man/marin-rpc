package com.manman.rpc.client.transport;

import com.manman.rpc.client.cache.LocalRpcResponseCache;
import com.manman.rpc.client.handler.RpcResponseHandler;
import com.manman.rpc.core.codec.RpcDecoder;
import com.manman.rpc.core.codec.RpcEncoder;
import com.manman.rpc.core.common.RpcRequest;
import com.manman.rpc.core.common.RpcResponse;
import com.manman.rpc.core.protocol.MessageProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Title: NettyNetClientTransport
 * @Author manman
 * @Description
 * @Date 2021/11/2
 */
@Slf4j
public class NettyNetClientTransport implements NetClientTransport {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final RpcResponseHandler handler;

    public NettyNetClientTransport() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        handler = new RpcResponseHandler();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                // 解码
                                .addLast(new RpcDecoder())
                                // 接收响应
                                .addLast(handler)
                                // 编码
                                .addLast(new RpcEncoder<>());
                    }
                });
    }

    @Override
    public MessageProtocol<RpcResponse> sendRequest(RequestMetaData metaData) throws Exception {
        MessageProtocol<RpcRequest> protocol = metaData.getProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> future = new RpcFuture<>();
        LocalRpcResponseCache.add(protocol.getHeader().getRequestId(), future);

        // TCP 连接
        ChannelFuture channelFuture = bootstrap.connect(metaData.getAddress(), metaData.getPort()).sync();
        channelFuture.addListener((ChannelFutureListener) arg0 -> {
            if (channelFuture.isSuccess()) {
                log.info("connect rpc server {} on port {} success", metaData.getAddress(), metaData.getPort());
            } else {
                log.error("connect rpc server {} on port {} failed", metaData.getAddress(), metaData.getPort());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        // 写入数据
        channelFuture.channel().writeAndFlush(protocol);
        return metaData.getTimeout() != null ? future.get(metaData.getTimeout(), TimeUnit.MILLISECONDS) : future.get();
    }
}
