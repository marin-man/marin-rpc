package com.manman.rpc.loadBalance.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.manman.rpc.loadBalance.LoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: RoundRobinRule
 * @Author manman
 * @Description 轮询算法
 * @Date 2021/8/29
 */
public class RoundRobinRule implements LoadBalancer {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 防止 Integer 越界，超过 Integer 最大值
     * @return
     */
    private final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        return next;
    }


    @Override
    public Instance getInstance(List<Instance> list) {
        int index = getAndIncrement() % list.size();
        return list.get(index);
    }
}
