package com.manman.rpc.consumer.controller;

import com.manman.rpc.api.service.HelloWorldService;
import com.manman.rpc.client.annotation.RpcAutowried;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title: HelloWorldController
 * @Author manman
 * @Description 消费者测试
 * @Date 2021/11/2
 */
@Controller
public class HelloWorldController {

    @RpcAutowried(version = "1.0")
    private HelloWorldService helloWorldService;

    @GetMapping("/hello")
    public ResponseEntity<String> pullServiceInfo(@RequestParam("name") String name) {
        return ResponseEntity.ok(helloWorldService.sayHello(name));
    }
}
