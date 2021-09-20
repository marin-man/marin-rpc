package com.manman.rpc.manager;

import com.alibaba.nacos.api.exception.NacosException;
import com.manman.rpc.config.RpcPropertiesConfig;
import com.manman.rpc.handler.HeartBeatClientHandler;
import com.manman.rpc.handler.RpcResponseMessageHandler;
import com.manman.rpc.message.RpcRequestMessage;
import com.manman.rpc.protocol.MessageCodecSharable;
import com.manman.rpc.protocol.ProtocolFrameDecoder;
import com.manman.rpc.register.nacos.NacosServerDiscovery;
import com.manman.rpc.register.ServerDiscovery;
import com.manman.rpc.register.zookeeper.ZkServerDiscovery;
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
    private static Bootstrap bootstrap = new Bootstrap();
    public static NioEventLoopGroup group = new NioEventLoopGroup();
    private ServerDiscovery serverDiscovery;

    // 根据序号 key 来判断是哪个请求的消息，value 是用来接收结果的 promise 对象
    public static final Map<Integer, Promise<Object>> PROMISES;

    // channel 集合，可能请求多个服务
    public static Map<String, Channel> channels;

    private static final Object LOCK = new Object();

    static {
        channels = new ConcurrentHashMap<>();
        initChannel();
        PROMISES = new ConcurrentHashMap<Integer, Promise<Object>>();
    }

    public RpcClientManager() {
        if ("nacos".equals(RpcPropertiesConfig.getRegisterType()))
            serverDiscovery = new NacosServerDiscovery(RpcPropertiesConfig.getLoadBalance());
        if ("zookeeper".equals(RpcPropertiesConfig.getRegisterType()))
            serverDiscovery = new ZkServerDiscovery(RpcPropertiesConfig.getLoadBalance());
    }

    /**
     * 获取 channel，没有就建立连接
     * @param inetSocketAddress
     * @return
     */
    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // 判断是否存在
        if (channels.containsKey(key)) {   // 判断是否有此管道
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
        InetSocketAddress service = serverDiscovery.getService(msg.getInterfaceName());  // 从注册中心获取服务地址
        Channel channel = get(service);   // 根据服务地址生成管道流
        if (!channel.isActive() || !channel.isRegistered()) {
            group.shutdownGracefully();
            return;
        }
        channel.writeAndFlush(msg).addListener(future -> {
            if (future.isSuccess())
                log.debug("客户端发送消息成功");
        });
        channel.close();    // 关闭管道
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
        HeartBeatClientHandler HEAT_BEAT_CLIENT = new HeartBeatClientHandler();
        bootstrap.channel(NioSocketChannel.class)      // bootstrap 模板设定 NIO 的网络管道模型
                .group(group)   // 设置文件描述符表（多路复用）
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)   // 连接超时时间
                .handler(new ChannelInitializer<SocketChannel>() {   // 设置处理句柄
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {  // 初始化管道
                        ch.pipeline().addLast(new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS));  // 设置心跳跳动时间
                        ch.pipeline().addLast(new ProtocolFrameDecoder());   // 定长解码器
                        ch.pipeline().addLast(MESSAGE_CODEC);   // 消息解析器
                        ch.pipeline().addLast(LOGGING_HANDLER);  // 日志处理器
                        ch.pipeline().addLast(HEAT_BEAT_CLIENT);  // 心跳数据包
                        ch.pipeline().addLast(RPC_HANDLER);  // 响应消息处理器
                    }
                });
        return bootstrap;
    }
}
