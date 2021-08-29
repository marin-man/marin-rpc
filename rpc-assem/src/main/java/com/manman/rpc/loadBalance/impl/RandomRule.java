package com.manman.rpc.loadBalance.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.manman.rpc.loadBalance.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * @Title: RandomRule
 * @Author manman
 * @Description 随机负载均衡算法
 * @Date 2021/8/29
 */
public class RandomRule implements LoadBalancer {

    private final Random random = new Random();

    /**
     * 随机获取实例
     * @param list
     * @return
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        return list.get(random.nextInt(list.size()));
    }
}
