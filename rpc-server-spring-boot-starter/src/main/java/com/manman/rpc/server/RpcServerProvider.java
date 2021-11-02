package com.manman.rpc.server;

import com.manman.rpc.core.common.ServiceInfo;
import com.manman.rpc.core.common.ServiceUtil;
import com.manman.rpc.core.register.RegistryService;
import com.manman.rpc.server.annotation.RpcService;
import com.manman.rpc.server.config.RpcServerProperties;
import com.manman.rpc.server.store.LocalServerCache;
import com.manman.rpc.server.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;

import java.net.InetAddress;

/**
 * @Title: RpcServerProvider
 * @Author manman
 * @Description 服务启动时扫描并注入到注册中心
 * @Date 2021/11/2
 */
@Slf4j
public class RpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private RegistryService registryService;

    private RpcServerProperties properties;

    private RpcServer rpcServer;

    public RpcServerProvider(RegistryService registryService, RpcServer rpcServer, RpcServerProperties properties) {
        this.registryService = registryService;
        this.properties = properties;
        this.rpcServer = rpcServer;
    }

    /**
     * 所有 bean 实例化之后处理
     * 暴露服务注册到注册中心
     * 容器启动后开启 netty 服务处理请求
     * @param bean
     * @param beanName
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            try {
                String serviceName = rpcService.interfaceType().getName();
                String version = rpcService.version();
                LocalServerCache.store(ServiceUtil.serviceKey(serviceName, version), bean);

                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(ServiceUtil.serviceKey(serviceName, version));
                serviceInfo.setPort(properties.getPort());
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setAppName(properties.getAppName());

                // 服务注册
                registryService.register(serviceInfo);
            } catch (Exception e) {
                log.error("服务注册出错:{}", e);
            }
        }
        return bean;
    }

    /**
     * 启动 rpc 服务，处理请求
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            rpcServer.start(properties.getPort());
        }).start();
        log.info("rpc server:{} start, appName: {}, port: {}", rpcServer, properties.getAppName(), properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 关闭之后把服务从 ZK 上清除
                registryService.destroy();
            } catch (Exception e) {
                log.error("", e);
            }
        }));
    }
}
