package com.mszlu.spring.aop.aspectj;

import com.mszlu.spring.aop.framework.AopProxyUtils;
import org.aopalliance.aop.Advice;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 执行切面的方法
 *
 */
public class AbstractAspectJAdvice implements Advice {

    private AspectJAdvice aspectJAdvice;

    public AbstractAspectJAdvice(AspectJAdvice aspectJAdvice) {
        this.aspectJAdvice = aspectJAdvice;
    }

    protected Object invokeAdviceMethod(ProceedingJoinPoint joinPoint, Object returnValue,
                                        Throwable tx) throws Throwable{
        Class<?> [] paramTypes =
                this.aspectJAdvice.getAspectMethod().getParameterTypes();
        if(paramTypes.length == 0){
            return this.aspectJAdvice.getAspectMethod().invoke(
                    aspectJAdvice.getAspectTarget());
        }else{
            Object [] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i ++) {
                if(paramTypes[i] == Throwable.class){
                    args[i] = tx;
                }else if(paramTypes[i] == Object.class){
                    args[i] = returnValue;
                }else if(paramTypes[i] == ProceedingJoinPoint.class){
                    args[i] = joinPoint;
                }
            }
            return this.aspectJAdvice.getAspectMethod().invoke(
                    AopProxyUtils.getTargetObject(aspectJAdvice.getAspectTarget()),args);
        }
    }
}
