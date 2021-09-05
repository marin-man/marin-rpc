package com.manman.rpc.register.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.manman.rpc.loadBalance.LoadBalancer;
import com.manman.rpc.register.ServerDiscovery;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Title: NacosServerDiscovery
 * @Author manman
 * @Description nacos 服务发现
 * @Date 2021/8/29
 */
public class NacosServerDiscovery implements ServerDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServerDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    /**
     * 根据服务名找到服务地址
     * @param serviceName
     * @return
     */
    @Override
    public InetSocketAddress getService(String serviceName) throws NacosException {
        List<Instance> instanceList = NacosUtils.getAllInstance(serviceName);
        if (instanceList.size() == 0)
            throw new RuntimeException("找不到对应的服务");
        Instance instance = loadBalancer.getInstance(instanceList);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }
}
