package com.manman.rpc.loadBalance;



import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: LoadBalancer
 * @Author manman
 * @Description 负载均衡算法
 * @Date 2021/8/29
 */
public interface LoadBalancer {
    Instance getInstance(List<Instance> list);

    enum Rule implements LoadBalancer {
        // 随机策略
        Random {
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
            public Instance getInstance(List<Instance> list) {
                int index = getAndIncrement() % list.size();
                return list.get(index);
            }
        }
    }
}
