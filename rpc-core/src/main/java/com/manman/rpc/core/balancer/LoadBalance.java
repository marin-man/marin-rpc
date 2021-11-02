package com.manman.rpc.core.balancer;

import com.manman.rpc.core.common.ServiceInfo;

import java.util.List;

/**
 * @Title: LoadBalance
 * @Author manman
 * @Description 负载均衡算法接口
 * @Date 2021/11/2
 */
public interface LoadBalance {

    ServiceInfo chooseOne(List<ServiceInfo> services);
}
