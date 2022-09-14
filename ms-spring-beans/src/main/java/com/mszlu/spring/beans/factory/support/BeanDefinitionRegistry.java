package com.mszlu.spring.beans.factory.support;

import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import com.mszlu.spring.beans.factory.NoSuchBeanDefinitionException;
import com.mszlu.spring.beans.factory.config.BeanDefinition;

/**
 * 定义了关于 BeanDefinition 的注册、移除、查询等一系列的操作
 *
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;

    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * Return the number of beans defined in the registry.
     * @return the number of beans defined in the registry
     */
    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

}