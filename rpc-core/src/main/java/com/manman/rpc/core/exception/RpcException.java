package com.manman.rpc.core.exception;

/**
 * @Title: RpcException
 * @Author manman
 * @Description rpc 操作异常
 * @Date 2021/11/2
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 3365624081242234230L;

    public RpcException() {
        super();
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
