package com.manman.rpc.client.transport;

import com.manman.rpc.core.common.RpcResponse;
import com.manman.rpc.core.protocol.MessageProtocol;

/**
 * @Title: NetClinetTransport
 * @Author manman
 * @Description 网络传输管理
 * @Date 2021/11/2
 */
public interface NetClientTransport {


    MessageProtocol<RpcResponse> sendRequest(RequestMetaData metaData) throws Exception;
}
