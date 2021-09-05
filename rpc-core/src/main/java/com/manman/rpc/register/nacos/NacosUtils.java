package com.manman.rpc.register.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.manman.rpc.config.RpcPropertiesConfig;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Title: NacosUtils
 * @Author manman
 * @Description nacos 工具类
 * @Date 2021/8/29
 */
public class NacosUtils {

    private static final NamingService namingService;

    private static final Set<String> serviceNames = new HashSet<>();

    private static InetSocketAddress address;

    private static final String SERVER_ADDR = RpcPropertiesConfig.getRegisterAddr();

    static {
        namingService = getNacosNamingService();
    }

    /**
     * 初始化
     * @return 返回命名空间
     */
    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);  // 连接 nacos
        } catch (NacosException e) {
            throw new RuntimeException("连接到 Nacos 时发送错误");
        }
    }

    /**
     * 注册服务
     * @param serverName 服务名
     * @param address 远程地址
     * @throws NacosException
     */
    public static void registerServer(String serverName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serverName, address.getHostName(), address.getPort());
        NacosUtils.address = address;   // 本服务的地址
        serviceNames.add(serverName);
    }

    /**
     * 获取当前服务名中的所有实例
     * @param serverName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serverName) throws NacosException {
        return namingService.getAllInstances(serverName);
    }

    /**
     * 注销服务
     */
    public static void clearRegister() {
        if (!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while (iterator.hasNext()) {
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    throw new RuntimeException("注销服务失败");
                }
            }
        }
        address = null;
    }
}
