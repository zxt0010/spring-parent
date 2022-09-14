package com.mszlu.spring.beans.factory.annotation;

import com.mszlu.spring.beans.factory.support.GenericBeanDefinition;

import java.lang.annotation.Annotation;

/**
 *
 */
public class AnnotatedGenericBeanDefinition extends GenericBeanDefinition {

    private Annotation[] annotations;

    public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
        this.setBeanClass(beanClass);
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
}
