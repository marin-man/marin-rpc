package com.manman.rpc.core.common;

import lombok.Data;

/**
 * @Title: RpcResponse
 * @Author manman
 * @Description 响应消息
 * @Date 2021/11/2
 */
@Data
public class RpcResponse {
    private Object data;
    private String message;
}
