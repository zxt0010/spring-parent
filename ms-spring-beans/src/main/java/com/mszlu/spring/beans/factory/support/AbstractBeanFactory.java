package com.mszlu.spring.beans.factory.support;

import com.mszlu.spring.beans.factory.BeanFactory;
import com.mszlu.spring.beans.factory.NoSuchBeanDefinitionException;
import com.mszlu.spring.beans.factory.config.BeanDefinition;

/**
 *
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory{

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    /**
     * 通过beanName检查bean工厂中是否包含bean的定义
     * @param beanName
     * @return
     */
    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * 创建一个bean的实例
     * @param beanName
     * @param beanDefinition
     * @param args
     * @return
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);


}
