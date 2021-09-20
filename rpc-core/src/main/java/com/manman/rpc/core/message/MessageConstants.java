package com.manman.rpc.core.message;

import lombok.Getter;

/**
 * @Title: MessageConstants
 * @Author manman
 * @Description
 * @Date 2021/9/20
 */
public class MessageConstants {
    public static final int HEADER_TOTAL_LEN = 42;  // 报头长度

    public static final short MAGIC = 0X12;   // 魔数

    public static final byte VERSION = 0X1;   // 版本

    public static final int REQ_LEN = 32;

    public enum MessageType {
        REQUEST((byte)101), RESPONSE((byte)102), PING((byte)103), PONG((byte)104);

        @Getter
        private byte type;

        MessageType(byte type) {
            this.type = type;
        }

        public static MessageType findByType(int type) {
            for (MessageType msgType : MessageType.values()) {
                if (msgType.getType() == type) {
                    return msgType;
                }
            }
            return null;
        }
    }
}
