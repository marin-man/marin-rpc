package com.manman.rpc.message;

/**
 * @Title: pONGmESSAGE
 * @Author manman
 * @Description
 * @Date 2021/8/26
 */
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PONG_MESSAGE_TYPE;
    }
}
