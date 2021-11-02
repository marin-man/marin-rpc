package com.manman.rpc.core.common;

/**
 * @Title: ServiceUtil
 * @Author manman
 * @Description 消息处理工具
 * @Date 2021/11/2
 */
public class ServiceUtil {

    /**
     * 将服务名和版本连接起来
     * @param serviceName
     * @param version
     * @return
     */
    public static String serviceKey(String serviceName, String version) {
        return String.join("-", serviceName, version);
    }
}
