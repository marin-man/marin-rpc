package com.manman.rpc.core.serialization;

/**
 * @Title: SerializationException
 * @Author manman
 * @Description 序列化异常
 * @Date 2021/11/2
 */
public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 3365624081242234230L;

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
