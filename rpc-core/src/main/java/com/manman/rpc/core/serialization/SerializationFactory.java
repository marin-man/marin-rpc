package com.manman.rpc.core.serialization;

/**
 * @Title: SerializationFactory
 * @Author manman
 * @Description 序列化工厂
 * @Date 2021/11/2
 */
public class SerializationFactory {

    public static RpcSerialization getRpcSerialization(SerializationTypeEnum typeEnum) {
        switch (typeEnum) {
            case HESSIAN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal");
        }
    }
}
