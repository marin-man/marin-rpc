package com.manman.rpc.core.serialization;

import java.io.IOException;

/**
 * @Title: RpcSerialization
 * @Author manman
 * @Description
 * @Date 2021/11/2
 */
public interface RpcSerialization {

    /**
     * 序列化
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;

}
