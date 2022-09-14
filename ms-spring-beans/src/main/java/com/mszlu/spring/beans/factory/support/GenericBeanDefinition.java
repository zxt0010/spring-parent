package com.mszlu.spring.beans.factory.support;

/**
 *
 */
public class GenericBeanDefinition extends AbstractBeanDefinition{


    private String initMethodName;

    private String destroyMethodName;

    private String scope = "";


    @Override
    public void setBeanClassName(String beanClassName) {

    }

    @Override
    public void setScope(String scope) {

    }

    @Override
    public void setDependsOn(String... dependsOn) {

    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {

    }

//    public Object getBeanClass() {
//        return beanClass;
//    }
//
//    public void setBeanClass(Object beanClass) {
//        this.beanClass = beanClass;
//    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getScope() {
        return scope;
    }
}
