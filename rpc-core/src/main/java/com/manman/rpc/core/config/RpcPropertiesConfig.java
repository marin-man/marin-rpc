package com.manman.rpc.core.config;

import com.manman.rpc.loadBalance.LoadBalancer;
import com.manman.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: RpcRropertiesConfig
 * @Author manman 配置扫描类
 * @Description
 * @Date 2021/9/20
 */
@Slf4j
@Configuration
public abstract class RpcPropertiesConfig {

    private static String loadBalance;   // 负载均衡策略
    private static String serializer;    // 序列化策略
    private static String serverPort;    // 服务器端口
    private static String registerType;  // 注册中心类型
    private static String registerAddr;  // 注册中心地址
    private static String registerNode;  // 注册中心节点


    @Value("${rpc.client.loadBalance:RoundRobin}")
    public void setLoadBalance(String loadBalance) {
        RpcPropertiesConfig.loadBalance = loadBalance;
    }

    @Value("${rpc.serializer:Protobuf}")
    public void setSerializer(String serializer) {
        RpcPropertiesConfig.serializer = serializer;
    }

    @Value("${rpc.server.port:8080}")
    public void setPort(String serverPort) {
        RpcPropertiesConfig.serverPort = serverPort;
    }

    @Value("${rpc.register.type:zookeeper}")
    public void setRegisterType(String registerType) {
        RpcPropertiesConfig.registerType = registerType;
    }

    @Value("${rpc.register.addr:zookeeper}")
    public void setRegisterAddr(String registerAddr) {
        RpcPropertiesConfig.registerAddr = registerAddr;
    }

    @Value("${rpc.register.node:/mini-rpc}")
    public void setRegisterNode(String registerNode) {
        RpcPropertiesConfig.registerNode = registerNode;
    }

    /**
     * 获取负载均衡策略
     */
    public static LoadBalancer.Rule getLoadBalance() {
        return LoadBalancer.Rule.valueOf(loadBalance);
    }

    /**
     * 获取序列化方式
     * @return
     */
    public static Serializer.Algorithm getSerializerAlogrithm() {
        return Serializer.Algorithm.valueOf(serializer);
    }

    /**
     * 获取配置的端口号
     * @return
     */
    public static int getServerPort() {
        return Integer.parseInt(serverPort);
    }

    /**
     * 获取注册中心类型
     * @return
     */
    public static String getRegisterType() {
        return registerType;
    }

    /**
     * 获取注册中心地址
     * @return
     */
    public static String getRegisterAddr() {
        return registerAddr;
    }

    /**
     * 获取所在注册中心的节点
     */
    public static String getRegisterNode() {
        return registerNode;
    }
}
