package com.manman.rpc.message;

import lombok.Data;
import lombok.ToString;

/**
 * @Title: RpcRequestMessage
 * @Author manman
 * @Description RPC请求消息封装
 * @Date 2021/8/26
 */
@Data
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    // 调用接口的全限定名，服务器根据他找到实现
    private String interfaceName;

    // 调用接口中的方法名
    private String methodName;

    // 方法返回类型
    private Class<?> returnType;

    // 方法参数类型数组
    private Class<?>[] parameterTypes;

    // 方法参数值数值
    private Object[] parameterValue;

    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType,
                             Class<?>[] parameterTypes, Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_REQUEST_TYPE;
    }
}
