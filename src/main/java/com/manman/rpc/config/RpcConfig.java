package com.manman.rpc.config;

import com.manman.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Title: RpcConfig
 * @Author manman
 * @Description 配置类
 * @Date 2021/8/26
 */
@Slf4j
public abstract class RpcConfig {
    static Properties properties;
    static {
        try (InputStream in = RpcConfig.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            log.debug("{}", e.getCause().getMessage());
        }
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

    public static Serializer.Algorithm getSerializerAlogrithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null)
            return Serializer.Algorithm.Java;
        return Serializer.Algorithm.valueOf(value);
    }
}
