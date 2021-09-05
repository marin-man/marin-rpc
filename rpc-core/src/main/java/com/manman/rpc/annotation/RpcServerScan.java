package com.manman.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: RpcServerScan
 * @Author manman
 * @Description RPC 接口扫描
 * @Date 2021/8/26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServerScan {
    String value() default "";
}
