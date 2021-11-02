package com.manman.rpc.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: ServiceInfo
 * @Author manman
 * @Description 服务信息
 * @Date 2021/11/2
 */
@Data
public class ServiceInfo implements Serializable {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 版本
     */
    private String version;

    /**
     * 地址
     */
    private String address;

    /**
     * 端口
     */
    private Integer port;
}
