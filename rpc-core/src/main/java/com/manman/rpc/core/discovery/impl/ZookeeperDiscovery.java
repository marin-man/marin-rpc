package com.manman.rpc.core.discovery.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.manman.rpc.core.balancer.LoadBalance;
import com.manman.rpc.core.common.ServiceInfo;
import com.manman.rpc.core.discovery.RpcServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @Title: ZookeeperDiscovery
 * @Author manman
 * @Description
 * @Date 2021/9/20
 */
@Slf4j
public class ZookeeperDiscovery implements RpcServiceDiscovery {

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;   // 最大重试次数
    public static final String ZK_BASE_PATH = "/mini_rpc";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    @Autowired
    private LoadBalance.Rule rule;

    public ZookeeperDiscovery(String registryAddr) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr,
                    new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            client.start();
            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(serializer)
                    .basePath(ZK_BASE_PATH)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            log.error("serviceDiscovery start error：{}", e);
        }
    }

    /**
     * 服务发现
     * @param serviceName
     * @return
     * @throws Exception
     */
    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return CollectionUtils.isEmpty(serviceInstances) ? null :
                rule.getServiceInfo(serviceInstances.stream().map(ServiceInstance::getPayload)
                .collect(Collectors.toList()));
    }
}
