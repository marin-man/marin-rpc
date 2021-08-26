package com.manman.rpc.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title: ServiceFactory
 * @Author manman
 * @Description 服务接口注入和发现
 * @Date 2021/8/26
 */
@Slf4j
public class ServiceFactory {

    /**
     * 保存所有有注解 @RpcService 的集合
     */
    public static final Map<String, Object> serviceFactory = new ConcurrentHashMap<>();

    /**
     * 添加已注解的类进入工厂
     * @param service 具体的 service
     * @param serviceName service 的名字
     * @param <T>
     */
    public <T> void addServiceProvider(T service, String serviceName) {
        if (serviceFactory.containsKey(serviceName))
            return;
        serviceFactory.put(serviceName, service);
        log.debug("服务类{}添加进工厂", serviceName);
    }

    /**
     * 从该方法获取远程调用接口
     * @param serviceName
     * @return
     */
    public Object getServiceProvider(String serviceName) {
        Object service = serviceFactory.get(serviceName);
        if (service == null)
            throw new RuntimeException("未发现服务");
        return service;
    }
}

