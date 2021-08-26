package com.manman.rpc.manager;

import com.manman.rpc.message.RpcRequestMessage;
import com.manman.rpc.message.RpcResponseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    //private final ServerDiscovery serverDiscovery;

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

//    public void sendRpcRequest(RpcRequestMessage msg) {
//        InetSocketAddress service = serviceDiscovery.getService(msg.getInterfaceName());
//        Channel channel = get(service);
//        if (!channel.isActive() || !channel.isRegistered()) {
//            group.shutdownGracefully();
//            return;
//        }
//        channel.writeAndFlush(msg).addListener(future -> {
//            if (future.isSuccess())
//                log.debug("客户端发送消息成功");
//        });
//    }
}
