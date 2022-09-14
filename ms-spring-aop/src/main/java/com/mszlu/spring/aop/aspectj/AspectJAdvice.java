package com.mszlu.spring.aop.aspectj;

import java.lang.reflect.Method;

/**
 * 切面属性
 *
 */
public class AspectJAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public AspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Method getAspectMethod() {
        return aspectMethod;
    }

    public void setAspectMethod(Method aspectMethod) {
        this.aspectMethod = aspectMethod;
    }

    public Object getAspectTarget() {
        return aspectTarget;
    }

    public void setAspectTarget(Object aspectTarget) {
        this.aspectTarget = aspectTarget;
    }
}
