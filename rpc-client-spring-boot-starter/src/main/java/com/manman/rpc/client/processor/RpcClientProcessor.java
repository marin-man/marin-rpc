package com.manman.rpc.client.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Title: RpcClientProperties
 * @Author manman
 * @Description bean 后置处理器，获取所有 bean
 * 判断 bean 字段是否被 @RpcServer 注释修饰
 * 动态修改被修饰的值为代理对象
 * @Date 2021/9/20
 */
public class RpcClientProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
