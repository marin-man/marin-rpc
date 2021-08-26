package com.manman.rpc.manager;

import com.manman.rpc.annotation.RpcServer;
import com.manman.rpc.annotation.RpcServerScan;
import com.manman.rpc.factory.ServiceFactory;
import com.manman.rpc.handler.HeartBeatServerHandler;
import com.manman.rpc.handler.PingMessageHandler;
import com.manman.rpc.handler.RpcRequestMessageHandler;
import com.manman.rpc.protocol.MessageCodecSharable;
import com.manman.rpc.protocol.ProtocolFrameDecoder;
import com.manman.rpc.utils.PackageScanUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Title: RpcServiceManager
 * @Author manman
 * @Description RPC服务端管理器
 * @Date 2021/8/26
 */
@Data
@Slf4j
public class RpcServiceManager {
    private String host;
    private int port;
    private ServiceFactory serviceFactory;
    NioEventLoopGroup worker = new NioEventLoopGroup();
    NioEventLoopGroup boss = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();

    public RpcServiceManager(String host, int port) {
        this.host = host;
        this.port = port;
        serviceFactory = new ServiceFactory();

        // 完成注册 扫描启动类上层的包，遍历所有 target 下的项目路径，找到加了注解的类，注册到 nacos 里
        autoRegistry();
    }

    /**
     * 开启服务
     */
    public void start() {
        // 日志
        LoggingHandler LOGGING = new LoggingHandler(LogLevel.DEBUG);
        // 消息解码器
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // RPC 请求处理器
        RpcRequestMessageHandler RPC_REQUEST_HANDLER = new RpcRequestMessageHandler();
        // 心跳处理器
        HeartBeatServerHandler HEAT_BEAT_SERVER = new HeartBeatServerHandler();
        // 心跳请求处理器
        PingMessageHandler PINGMESSAGE = new PingMessageHandler();

        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ProtocolFrameDecoder()); // 定长解码器
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(LOGGING);
                            ch.pipeline().addLast(RPC_REQUEST_HANDLER);
                        }
                    });
            // 绑定断开
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.debug("{}", e.getCause().getMessage());
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    /**
     * 自动扫描 @RpcServer 注解，注册服务
     */
    public void autoRegistry() {
        String mainClassPath = PackageScanUtils.getStackTrace();
        Class<?> mainClass;
        try {
            mainClass = Class.forName(mainClassPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("启动类未找到");
        }
        if (mainClass.isAnnotationPresent(RpcServer.class)) {
            throw new RuntimeException("启动类缺少 @RpcServer 注解");
        }
        String annotationValue = mainClass.getAnnotation(RpcServerScan.class).value();
        // 如果注解路径的值是空，则等于 main 父路径包下，获取父路径
        if ("".equals(annotationValue)) {
            annotationValue = mainClassPath.substring(0, mainClassPath.lastIndexOf("."));
        }
        // 获取所有类的 set 集合
        Set<Class<?>> set = PackageScanUtils.getClasses(annotationValue);
        for (Class<?> c : set) {
            // 只有有 @RpcServer 注解的才注册
            if (c.isAnnotationPresent(RpcServer.class)) {
                String serverNameValue = c.getAnnotation(RpcServer.class).name();
                Object object;
                try {
                    object = c.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    continue;
                }
                // 注解的值如果为空，使用类名
                if ("".equals(serverNameValue)) {
                    addServer(object, c.getCanonicalName());
                } else {
                    addServer(object, serverNameValue);
                }
            }
        }
    }

    /**
     * 添加对象到工厂和注册到注册中心
     * @param object
     * @param serverNameValue
     */
    private void addServer(Object object, String serverNameValue) {
        serviceFactory.addServiceProvider(object, serverNameValue);
    }
}
