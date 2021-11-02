package com.manman.rpc.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: RpcService
 * @Author manman
 * @Description 暴露服务注解
 * @Date 2021/11/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RpcService {

    /**
     * 暴露服务接口类型
     * @return
     */
    Class<?> interfaceType() default Object.class;

    /**
     * 服务版本
     * @return
     */
    String version() default "1.0";
}
