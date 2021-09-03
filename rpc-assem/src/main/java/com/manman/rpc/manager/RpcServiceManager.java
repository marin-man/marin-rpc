package com.manman.rpc.manager;

import com.manman.rpc.annotation.RpcServer;
import com.manman.rpc.annotation.RpcServerScan;
import com.manman.rpc.factory.ServerFactory;
import com.manman.rpc.handler.HeartBeatServerHandler;
import com.manman.rpc.handler.PingMessageHandler;
import com.manman.rpc.handler.RpcRequestMessageHandler;
import com.manman.rpc.protocol.MessageCodecSharable;
import com.manman.rpc.protocol.ProtocolFrameDecoder;
import com.manman.rpc.register.NacosServerRegistry;
import com.manman.rpc.register.ServerRegistry;
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

import java.net.InetSocketAddress;
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
    private ServerRegistry serverRegistry;
    private ServerFactory serverFactory;
    NioEventLoopGroup worker = new NioEventLoopGroup();
    NioEventLoopGroup boss = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();

    public RpcServiceManager(String host, int port) {
        this.host = host;
        this.port = port;
        serverFactory = new ServerFactory();
        serverRegistry = new NacosServerRegistry();

        // 完成注册 扫描启动类上层的包，遍历所有 target 下的项目路径，找到加了注解的类，注册到 nacos 里
        autoRegistry();
    }

    /**
     * 开启服务
     */
    public void start() {
        // 日志
        final LoggingHandler LOGGING = new LoggingHandler(LogLevel.DEBUG);
        // 消息解码器
        final MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // RPC 请求处理器
        final RpcRequestMessageHandler RPC_REQUEST_HANDLER = new RpcRequestMessageHandler();
        // 心跳处理器
        final HeartBeatServerHandler HEAT_BEAT_SERVER = new HeartBeatServerHandler();
        // 心跳请求处理器
        final PingMessageHandler PING_MESSAGE = new PingMessageHandler();

        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)   // 对应 tcp/ip协议 listen函数中的backlog参数，同一时间只能处理一个客户端连接，多个客户端来的时候，客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_KEEPALIVE, true)  // 对应于套接字选项中的SO_KEEPALIVE，该参数用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的连接,如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
                    .option(ChannelOption.TCP_NODELAY, true)  // 对应于套接字选项中的TCP_NODELAY,该参数的使用与Nagle算法有关。Nagle算法是将小的数据包组装为更大的帧然后进行发送，而不是输入一次发送一次，因此在数据包不足的时候会等待其他数据的到来，组装成大的数据包进行发送，虽然该算法有效提高了网络的有效负载，但是却造成了延时。 而该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ProtocolFrameDecoder()); // 定长解码器
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(LOGGING);
                            ch.pipeline().addLast(RPC_REQUEST_HANDLER);
                            ch.pipeline().addLast(HEAT_BEAT_SERVER);    // 心跳处理器
                            ch.pipeline().addLast(PING_MESSAGE);
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
        String mainClassPath = PackageScanUtils.getStackTrace(); // 获取到 main 的路径 com.manman.rpc.RpcServer
        Class<?> mainClass;
        try {
            mainClass = Class.forName(mainClassPath);  // 获取 RpcServer 的 class
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("启动类未找到");
        }
        if (!mainClass.isAnnotationPresent(RpcServerScan.class)) {
            throw new RuntimeException("启动类缺少 @RpcServerScan 注解");
        }
        String annotationValue = mainClass.getAnnotation(RpcServerScan.class).value();
        // 如果注解路径的值是空，则等于 main 父路径包下，获取父路径
        if ("".equals(annotationValue)) {
            annotationValue = mainClassPath.substring(0, mainClassPath.lastIndexOf("."));   // 有扫描注解，截取当前类路径下的包前缀：com.manman.rpc
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
     * @param server
     * @param serverNameValue
     */
    private <T> void addServer(T server, String serverNameValue) {
        serverRegistry.register(serverNameValue, new InetSocketAddress(host, port));
        serverFactory.addServiceProvider(server, serverNameValue);
    }
}
