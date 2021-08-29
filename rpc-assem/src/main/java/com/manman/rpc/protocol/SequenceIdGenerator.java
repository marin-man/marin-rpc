package com.manman.rpc.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: SequenceIdGenerator
 * @Author manman
 * @Description 生成序列化id
 * @Date 2021/8/29
 */
public class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
