package com.manman.rpc.manager;

import com.alibaba.nacos.api.exception.NacosException;
import com.manman.rpc.handler.HeartBeatServerHandler;
import com.manman.rpc.handler.RpcResponseMessageHandler;
import com.manman.rpc.loadBalance.impl.RoundRobinRule;
import com.manman.rpc.message.RpcRequestMessage;
import com.manman.rpc.protocol.MessageCodecSharable;
import com.manman.rpc.protocol.ProtocolFrameDecoder;
import com.manman.rpc.register.NacosServerDiscovery;
import com.manman.rpc.register.ServerDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Title: RpcClientManager
 * @Author manman
 * @Description RPC客户端管理器
 * @Date 2021/8/26
 */
@Slf4j
public class RpcClientManager {
    // 单例 channel
    private static final Bootstrap bootstrap;
    public static NioEventLoopGroup group;
    private final ServerDiscovery serverDiscovery;

    // 根据序号 key 来判断是哪个请求的消息，value 是用来接收结果的 promise 对象
    public static final Map<Integer, Promise<Object>> PROMISES;

    // channel 集合，可能请求多个服务
    public static Map<String, Channel> channels;

    private static final Object LOCK = new Object();

    static {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        channels = new ConcurrentHashMap<>();
        PROMISES = new ConcurrentHashMap<Integer, Promise<Object>>();
        initChannel();
    }

    public RpcClientManager() {
        this.serverDiscovery = new NacosServerDiscovery(new RoundRobinRule());
    }

    /**
     * 获取 channel，没有就建立连接
     * @param inetSocketAddress
     * @return
     */
    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // 判断是否存在
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channel != null && channel.isActive())
                return channel;
            channels.remove(key);
        }
        // 建立连接
        Channel channel = null;
        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
            channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    log.debug("断开连接");
                }
            });
        } catch (InterruptedException e) {
            channel.close();
            log.debug("连接客户端出错" + e);
            return null;
        }
        channels.put(key, channel);
        return channel;
    }

    /**
     * 根据用户名发送消息  服务发现，找到地址
     * @param msg
     */
    public void sendRpcRequest(RpcRequestMessage msg) throws NacosException {
        InetSocketAddress service = serverDiscovery.getService(msg.getInterfaceName());
        Channel channel = get(service);
        if (!channel.isActive() || !channel.isRegistered()) {
            group.shutdownGracefully();
            return;
        }
        channel.writeAndFlush(msg).addListener(future -> {
            if (future.isSuccess())
                log.debug("客户端发送消息成功");
        });
    }

    /**
     * 初始化 channel 方法
     * @return
     */
    private static Bootstrap initChannel() {
        // 日志 handler
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        // 消息处理 handler
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // 处理相应 handler
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        // 心跳处理器
        HeartBeatServerHandler HEATBEAT_CLIENT = new HeartBeatServerHandler();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS));
                        // 定长解码器
                        ch.pipeline().addLast(new ProtocolFrameDecoder());
                        ch.pipeline().addLast(MESSAGE_CODEC);
                        ch.pipeline().addLast(LOGGING_HANDLER);
                        ch.pipeline().addLast(HEATBEAT_CLIENT);
                        ch.pipeline().addLast(RPC_HANDLER);
                    }
                });
        return bootstrap;
    }
}
