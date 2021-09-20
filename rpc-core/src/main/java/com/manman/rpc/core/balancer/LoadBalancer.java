package com.manman.rpc.core.balancer;

import com.manman.rpc.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: LoadBalance
 * @Author manman
 * @Description
 * @Date 2021/9/20
 */
public interface LoadBalancer {
    ServiceInfo getServiceInfo(List<ServiceInfo> list);

    enum Rule implements LoadBalancer {
        // 随机策略
        Random {
            private final java.util.Random random = new Random();
            /**
             * 随机获取实例
             * @param list
             * @return
             */

            public ServiceInfo getServiceInfo(List<ServiceInfo> list) {
                return list.get(random.nextInt(list.size()));
            }
        },

        // 轮询策略
        RoundRobin {
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
            public ServiceInfo getServiceInfo(List<ServiceInfo> list) {
                int index = getAndIncrement() % list.size();
                return list.get(index);
            }
        }
    }
}
