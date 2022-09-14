package com.mszlu.spring.context.annotation;

import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;


public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }
}
