package com.manman.rpc.register.zookeeper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.manman.rpc.config.RpcPropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Title: ZookeeperUtils
 * @Author manman
 * @Description
 * @Date 2021/9/5
 */
@Slf4j
public class ZookeeperUtils {

    private static ZkClient zkClient;
    private static final List<String> addressCache = new CopyOnWriteArrayList<>();

    private static final String REGISTER_ADDR = RpcPropertiesConfig.getRegisterAddr();

    static {
        zkClient = new ZkClient(REGISTER_ADDR, 30000, 5000);
        log.debug("已连接 zookeeper.");
    }


    /**
     * 注册服务
     * @param serverName 服务名
     * @param address 远程地址
     */
    public static void registerServer(String serverName, InetSocketAddress address) {
        // 创建 registry 节点（持久）
        String registryPath = RpcPropertiesConfig.getRegisterNode();
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            log.debug("创建注册节点 {}" + registryPath);
        }
        // 创建 service 节点（持久）
        String servicePath = registryPath + "/" + serverName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.debug("创建服务节点 {}" + servicePath);
        }

        // 创建 address 节点（临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, address);
        log.debug("创建 address 节点 {}", addressNode);
    }

    /**
     * 获取当前服务名中的所有实例
     * @param serverName
     * @return
     */
    public static List<String> getAllInstance(String serverName) {
        String servicePath = RpcPropertiesConfig.getRegisterNode() + "/" + serverName;
        // 获取服务节点
        if (!zkClient.exists(servicePath)) {
            throw new RuntimeException("can't find any service node on path " + servicePath);
        }

        // 从本地缓存获取某个服务地址
        if (addressCache.size() > 0)
            return addressCache;

        // 监听 servicePath 下的文件是否发生变化
        zkClient.subscribeChildChanges(servicePath, (parentPath, currentChilds) -> {
            log.debug("servicePath is changed: {}", parentPath);
            addressCache.clear();
            addressCache.addAll(currentChilds);
        });

        List<String> addressList = zkClient.getChildren(servicePath);
        addressCache.addAll(addressList);
        if (CollectionUtils.isEmpty(addressList)) {
            throw new RuntimeException("can't find any address node on path " + servicePath);
        }
        for (String address : addressList)
            System.out.println(address);
        return addressList;
    }

}
