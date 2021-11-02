package com.manman.rpc.core.register;

import com.manman.rpc.core.common.ServiceInfo;

import java.io.IOException;

/**
 * @Title: RegistryService
 * @Author manman
 * @Description 服务注册
 * @Date 2021/11/2
 */
public interface RegistryService {

    void register(ServiceInfo serviceInfo) throws Exception;

    void unRegister(ServiceInfo serviceInfo) throws Exception;

    void destroy() throws IOException;
}
