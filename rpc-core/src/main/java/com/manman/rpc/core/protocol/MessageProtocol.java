package com.manman.rpc.core.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: MessageProtocol
 * @Author manman
 * @Description 消息协议
 * @Date 2021/11/2
 */
@Data
public class MessageProtocol<T> implements Serializable {

    /**
     *  消息头
     */
    private MessageHeader header;

    /**
     *  消息体
     */
    private T body;
}
