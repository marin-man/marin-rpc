package com.manman.rpc.loadBalance;



import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @Title: LoadBalancer
 * @Author manman
 * @Description 负载均衡算法
 * @Date 2021/8/29
 */
public interface LoadBalancer {
    Instance getInstance(List<Instance> list);
}
