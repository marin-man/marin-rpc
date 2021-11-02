package com.manman.rpc.client.transport;

import com.manman.rpc.core.common.RpcRequest;
import com.manman.rpc.core.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: RequestMetaData
 * @Author manman
 * @Description 请求元数据
 * @Date 2021/11/2
 */
@Data
@Builder
public class RequestMetaData implements Serializable {
    /**
     *  协议
     */
    private MessageProtocol<RpcRequest> protocol;

    /**
     *  地址
     */
    private String address;

    /**
     *  端口
     */
    private Integer port;

    /**
     *  服务调用超时
     */
    private Integer timeout;
}
