package com.manman.rpc.core.balancer;

import com.manman.rpc.core.common.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Title: FullRoundBalance
 * @Author manman
 * @Description 轮询算法
 * @Date 2021/11/2
 */
@Slf4j
public class FullRoundBalance implements LoadBalance {

    private int index;


    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        // 加锁防止多线程情况下，index 超出 service.size(
        if (index >= services.size()) {
            index = 0;
        }
        return services.get(index++);
    }
}
