package com.manman.rpc.core.protocol;

/**
 * @Title: ProtocolConstants
 * @Author manman
 * @Description 消息常量
 * @Date 2021/11/2
 */
public class ProtocolConstants {

    public static final int HEADER_TOTAL_LEN = 42;

    public static final short MAGIC = 0x00;

    public static final byte VERSION = 0x1;

    public static final int REQ_LEN = 32;
}
