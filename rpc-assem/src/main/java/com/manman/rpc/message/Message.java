package com.manman.rpc.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Message
 * @Author manman
 * @Description 消息封装包
 * @Date 2021/8/26
 */
@Data
public abstract class Message implements Serializable {

    // 每个消息会带一个序号，用于标记信息，可用于多线程间进行通信
    private int sequenceId;

    // 消息类型
    private int messageType;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    public static final int RPC_MESSAGE_REQUEST_TYPE = 101;
    public static final int RPC_MESSAGE_RESPONSE_TYPE = 102;
    public static final int PING_MESSAGE_TYPE = 103;
    public static final int PONG_MESSAGE_TYPE = 104;

    static {
        messageClasses.put(RPC_MESSAGE_REQUEST_TYPE, RpcRequestMessage.class);
        messageClasses.put(RPC_MESSAGE_RESPONSE_TYPE, RpcResponseMessage.class);
        messageClasses.put(PING_MESSAGE_TYPE, PingMessage.class);
        messageClasses.put(PONG_MESSAGE_TYPE, PongMessage.class);
    }

    /**
     * 根据消息类型字节，获取相应的消息 class
     * @param messageType
     * @return
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }
}
