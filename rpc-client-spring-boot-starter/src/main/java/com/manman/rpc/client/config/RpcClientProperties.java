package com.manman.rpc.client.config;

import lombok.Data;

/**
 * @Title: RpcClientProperties
 * @Author manman
 * @Description 客户端配置映射
 * @Date 2021/11/2
 */
@Data
public class RpcClientProperties {

    /**
     * 负载均衡
     */
    private String balance;

    /**
     * 序列化
     */
    private String serialization;

    /**
     * 服务发现地址
     */
    private String discoveryAddr = "127.0.0.1:2181";

    /**
     * 服务调用超时
     */
    private Integer timeout;
}
