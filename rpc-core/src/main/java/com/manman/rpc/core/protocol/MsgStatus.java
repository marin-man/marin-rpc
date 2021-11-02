package com.manman.rpc.core.protocol;


import lombok.Getter;

/**
 * @Title: MsgStatus
 * @Author manman
 * @Description 请求状态码
 * @Date 2021/11/2
 */
public enum MsgStatus {
    /**
     * 成功
     */
    SUCCESS((byte) 0),

    /**
     * 失败
     */
    FALL((byte) 1);

    @Getter
    private final byte code;

    MsgStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code) {
        return MsgStatus.SUCCESS.code == code;
    }
}
