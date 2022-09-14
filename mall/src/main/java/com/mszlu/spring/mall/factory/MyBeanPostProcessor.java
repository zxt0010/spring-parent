package com.mszlu.spring.mall.factory;


import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.config.BeanPostProcessor;

/**
 *
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("after");
        return bean;
    }
}