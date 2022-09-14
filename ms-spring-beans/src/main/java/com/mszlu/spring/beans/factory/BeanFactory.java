package com.mszlu.spring.beans.factory;


import com.mszlu.spring.beans.BeansException;

/**
 * 定义IOC容器的基本功能接口
 *
 */
public interface BeanFactory {
    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object getBean(String beanName) throws BeansException;

    /**
     * 是否为单例
     * @param beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 是否为原始类型（是否为多例模式）
     * @param beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException;

}