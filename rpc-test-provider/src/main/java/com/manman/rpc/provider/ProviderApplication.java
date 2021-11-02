package com.manman.rpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Title: ProviderApplication
 * @Author manman
 * @Description
 * @Date 2021/11/2
 */
@SpringBootApplication
@ComponentScan("com.manman.rpc")
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
