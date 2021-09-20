package com.manman.rpc.client.config;

import com.manman.rpc.client.processor.RpcClientProcessor;
import com.manman.rpc.core.config.RpcPropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: RpcRropertiesConfig
 * @Author manman 配置扫描类
 * @Description
 * @Date 2021/9/20
 */
@Slf4j
@Configuration
public abstract class RpcClientPropertiesConfig extends RpcPropertiesConfig {

    @Bean
    public RpcClientProcessor rpcClientProperties() {
        return new RpcClientProcessor();
    }
}
