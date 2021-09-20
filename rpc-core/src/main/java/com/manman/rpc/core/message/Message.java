package com.manman.rpc.core.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: Message
 * @Author manman
 * @Description 消息协议
 * @Date 2021/9/20
 */
@Data
public class Message<T> implements Serializable {

    // 消息头
    private MessageHeader header;

    // 消息体
    private T body;
}
