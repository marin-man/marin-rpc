package com.manman.rpc.config;

import com.manman.rpc.loadBalance.LoadBalancer;
import com.manman.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Title: RpcConfig
 * @Author manman
 * @Description 获取配置类信息，实现在配置类中绑定基本信息
 * @Date 2021/8/26
 */
@Slf4j
public abstract class RpcPropertiesConfig {
    static Properties properties;
    static {
        try (InputStream in = RpcPropertiesConfig.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            log.debug("{}", e.getCause().getMessage());
        }
    }

    /**
     * 获取负载均衡策略
     */
    public static LoadBalancer.Rule getLoadBalance() {
        String value = properties.getProperty("server.loadBalance");
        if (value == null)
            return LoadBalancer.Rule.RoundRobin;
        return LoadBalancer.Rule.valueOf(value);
    }

    /**
     * 获取序列化方式
     * @return
     */
    public static Serializer.Algorithm getSerializerAlogrithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null)
            return Serializer.Algorithm.Java;
        return Serializer.Algorithm.valueOf(value);
    }

    /**
     * 获取配置的端口号
     * @return
     */
    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if (value == null)
            return 8080;
        return Integer.parseInt(value);
    }

    /**
     * 获取注册中心类型
     * @return
     */
    public static String getRegisterType() {
        String value = properties.getProperty("register.type");
        if (value == null)
            return "nacos";
        return value;
    }

    /**
     * 获取注册中心地址
     * @return
     */
    public static String getRegisterAddr() {
        String value = properties.getProperty("register.addr");
        if (value == null)
            return "127.0.0.1";
        return value;
    }
}
