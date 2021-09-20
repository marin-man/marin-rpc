package com.manman.rpc.core.message;

import com.manman.rpc.core.config.RpcPropertiesConfig;
import com.manman.rpc.serialize.Serializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Title: MessageHeader
 * @Author manman
 * @Description 消息头
 * @Date 2021/9/20
 */
@Data
public class MessageHeader implements Serializable {

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 32byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    */

    // 魔数
    private short magic;

    // 协议版本号
    private byte version;

    // 序列化算法
    private byte serialization;

    // 报文类型
    private byte msgType;

    // 状态
    private byte status;

    // 消息 ID
    private String requestId;

    // 数据长度
    private int msgLen;

    public static MessageHeader build(String serialization){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMagic(MessageConstants.MAGIC);
        messageHeader.setVersion(MessageConstants.VERSION);
        messageHeader.setRequestId(UUID.randomUUID().toString().replaceAll("-",""));
        messageHeader.setMsgType(MessageConstants.MessageType.REQUEST.getType());
        messageHeader.setSerialization((byte) RpcPropertiesConfig.getSerializerAlogrithm().ordinal());
        return messageHeader;
    }
}
