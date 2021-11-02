package com.manman.rpc.core.exception;

/**
 * @Title: ResourceNotFountException
 * @Author manman
 * @Description 资源未发现异常
 * @Date 2021/11/2
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3365624081242234230L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
