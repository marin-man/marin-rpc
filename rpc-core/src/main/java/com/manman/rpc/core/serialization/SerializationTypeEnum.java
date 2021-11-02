package com.manman.rpc.core.serialization;

import lombok.Getter;

/**
 * @Title: SerializationTypeEnum
 * @Author manman
 * @Description 序列化类型
 * @Date 2021/11/2
 */
public enum SerializationTypeEnum {

    HESSIAN((byte) 0),
    JSON((byte) 1);

    @Getter
    private byte type;

    SerializationTypeEnum(byte type) {
        this.type = type;
    }

    public static SerializationTypeEnum parseByName(String typeName) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(typeName)) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

    public static SerializationTypeEnum parseByType(byte type) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }
}
