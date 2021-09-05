package com.manman.rpc.message;

/**
 * @Title: PingMessage
 * @Author manman
 * @Description 客户端维持心跳发送 ping 数据包
 * @Date 2021/8/26
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PING_MESSAGE_TYPE;
    }
}
