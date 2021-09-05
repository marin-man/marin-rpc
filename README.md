# Mini-RPC

## Overview
Mini-RPC 是一款高可用的 java RPC 框架。

## 特色
* 支持多种序列化方式：protocol, gson, fastjson, jdk 默认序列化方式。
* 支持多种负载均衡策略：轮询，随机。
* 支持多种注册中心：nacos。
* 通过配置文件可以快速配置，支持默认配置，实现一键启动。
* 支持 SPI 注解方式，只需添加一行注解就能实现远程调用。