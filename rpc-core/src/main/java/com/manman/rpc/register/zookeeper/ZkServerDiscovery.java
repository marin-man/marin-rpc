package com.manman.rpc.register.zookeeper;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.manman.rpc.loadBalance.LoadBalancer;
import com.manman.rpc.register.ServerDiscovery;
import com.manman.rpc.register.nacos.NacosUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Title: ZkServiceDiscovery
 * @Author manman
 * @Description
 * @Date 2021/9/5
 */
public class ZkServerDiscovery implements ServerDiscovery {

    private final LoadBalancer loadBalancer;

    public ZkServerDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    /**
     * 根据服务名找到服务地址
     * @param serviceName
     * @return
     */
    @Override
    public InetSocketAddress getService(String serviceName) throws NacosException {
        List<String> instanceList = ZookeeperUtils.getAllInstance(serviceName);
        if (instanceList.size() == 0)
            throw new RuntimeException("找不到对应的服务");
        return new InetSocketAddress("0", 0);
    }
}
