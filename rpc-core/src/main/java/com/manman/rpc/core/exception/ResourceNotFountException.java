package com.manman.rpc.core.exception;

/**
 * @Title: ResourceNotFountException
 * @Author manman
 * @Description 资源未发现异常
 * @Date 2021/9/20
 */
public class ResourceNotFountException extends RuntimeException {
    private static final long serialVersionUID = 3365624081242234230L;

    public ResourceNotFountException() {
        super();
    }

    public ResourceNotFountException(String msg) {
        super(msg);
    }

    public ResourceNotFountException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFountException(Throwable cause) {
        super(cause);
    }
}
