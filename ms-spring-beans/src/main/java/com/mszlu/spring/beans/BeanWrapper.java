package com.mszlu.spring.beans;

/**
 *
 */
public class BeanWrapper {

    private Object bean;

    private boolean isAop;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public boolean isAop() {
        return isAop;
    }

    public void setAop(boolean aop) {
        isAop = aop;
    }
}
