package com.manman.rpc.core.balancer;

import com.manman.rpc.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * @Title: RandomBalance
 * @Author manman
 * @Description 随机算法
 * @Date 2021/11/2
 */
public class RandomBalance implements LoadBalance {

    private static Random random = new Random();


    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        return services.get(random.nextInt(services.size()));
    }
}
