package com.mszlu.spring.context.annotation;

import com.mszlu.spring.beans.factory.annotation.AnnotatedGenericBeanDefinition;

import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mszlu.spring.beans.factory.xml.BeanDefinitionHolder;


import java.lang.annotation.Annotation;


public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    private void registerBean(Class<?> beanClass) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        String beanName = abd.getBeanClassName();
        Annotation[] annotations = beanClass.getAnnotations();
        abd.setAnnotations(annotations);
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(abd,beanName);
        registry.registerBeanDefinition(beanName,beanDefinitionHolder.getBeanDefinition());
    }
}
