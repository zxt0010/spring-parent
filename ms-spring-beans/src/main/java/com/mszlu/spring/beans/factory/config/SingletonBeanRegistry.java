package com.mszlu.spring.beans.factory.config;


public interface SingletonBeanRegistry {
    /**
     * 根据名字 注册单例bean
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 根据beanName 获取单例bean
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * beanName是否被注册
     * @param beanName
     * @return
     */
    boolean containsSingleton(String beanName);
}
