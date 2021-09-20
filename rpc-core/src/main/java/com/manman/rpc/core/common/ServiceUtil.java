package com.manman.rpc.core.common;

/**
 * @Title: ServiceUtil
 * @Author manman
 * @Description 服务信息工具类
 * @Date 2021/9/20
 */
public class ServiceUtil {

    /**
     * 拼接服务名和版本号
     * @param serviceName
     * @param version
     * @return
     */
    public static String serviceKey(String serviceName, String version) {
        return String.join("-", serviceName, version);
    }
}
