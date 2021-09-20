package com.manman.rpc.core.discovery;

import com.manman.rpc.core.common.ServiceInfo;

/**
 * @Title: ServiceDiscovery
 * @Author manman
 * @Description 服务发现类
 * @Date 2021/9/20
 */
public interface RpcServiceDiscovery {

    /**
     * 更加服务名找到服务信息
     * @param serviceName
     * @return
     * @throws Exception
     */
    ServiceInfo discovery(String serviceName) throws Exception;
}
