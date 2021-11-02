package com.manman.rpc.core.common;

import lombok.Data;

/**
 * @Title: RpcRequest
 * @Author manman
 * @Description 消息请求
 * @Date 2021/11/2
 */
@Data
public class RpcRequest {

    /**
     * 请求的服务名 + 版本
     */
    private String serviceName;

    /**
     * 请求调用的方法
     */
    private String method;

    /**
     *  参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     *  参数
     */
    private Object[] parameters;
}
