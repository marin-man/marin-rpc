package com.manman.rpc.server.config;

import com.manman.rpc.core.register.RegistryService;
import com.manman.rpc.core.register.ZookeeperRegistryService;
import com.manman.rpc.server.RpcServerProvider;
import com.manman.rpc.server.transport.NettyRpcServer;
import com.manman.rpc.server.transport.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: RpcServerAutoConfiguration
 * @Author manman
 * @Description rpc server 自动装配
 * @Date 2021/11/2
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Autowired
    private RpcServerProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RegistryService registryService() {
        return new ZookeeperRegistryService(properties.getRegistryAddr());
    }

    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    RpcServer rpcServer() {
        return new NettyRpcServer();
    }

    @Bean
    @ConditionalOnMissingBean(RpcServerProvider.class)
    RpcServerProvider rpcServerProvider(@Autowired RegistryService registryService,
                                        @Autowired RpcServer rpcServer,
                                        @Autowired RpcServerProperties rpcServerProperties) {
        return new RpcServerProvider(registryService, rpcServer, rpcServerProperties);
    }

}
