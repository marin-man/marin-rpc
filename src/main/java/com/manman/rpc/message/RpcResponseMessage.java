package com.manman.rpc.message;

import lombok.Data;
import lombok.ToString;

/**
 * @Title: RpcResponseMessage
 * @Author manman
 * @Description RPC消息返回封装
 * @Date 2021/8/26
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {

    // 返回值
    private Object returnValue;

    // 异常值
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_RESPONSE_TYPE;
    }
}
