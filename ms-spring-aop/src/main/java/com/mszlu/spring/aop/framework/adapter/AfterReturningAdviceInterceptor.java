package com.mszlu.spring.aop.framework.adapter;

import com.mszlu.spring.aop.aspectj.AbstractAspectJAdvice;
import com.mszlu.spring.aop.aspectj.AspectJAdvice;
import org.aopalliance.interceptor.MethodInterceptor;
import org.aopalliance.interceptor.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 */
public class AfterReturningAdviceInterceptor extends AbstractAspectJAdvice implements MethodInterceptor {

    private ProceedingJoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(AspectJAdvice aspectJAdvice) {
        super(aspectJAdvice);
    }

    private void afterReturning(Object retVal, Method method, Object[] args,
                                Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object retVal = methodInvocation.proceed();
        this.joinPoint = methodInvocation;
        // 后置通知
        this.afterReturning(retVal,methodInvocation.getMethod(),
                methodInvocation.getArguments(),methodInvocation.getThis());
        return retVal;
    }

}
