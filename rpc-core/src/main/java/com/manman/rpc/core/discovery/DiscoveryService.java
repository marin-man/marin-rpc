package com.manman.rpc.core.discovery;

import com.manman.rpc.core.common.ServiceInfo;

/**
 * @Title: DiscoveryService
 * @Author manman
 * @Description 服务发现
 * @Date 2021/11/2
 */
public interface DiscoveryService {

    /**
     * 发现服务
     * @param serviceName
     * @return
     * @throws Exception
     */
    ServiceInfo discovery(String serviceName) throws Exception;
}
