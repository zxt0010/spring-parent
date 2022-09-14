package com.mszlu.spring.aop.framework.adapter;

import com.mszlu.spring.aop.aspectj.AbstractAspectJAdvice;
import com.mszlu.spring.aop.aspectj.AspectJAdvice;
import org.aopalliance.interceptor.MethodInterceptor;
import org.aopalliance.interceptor.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 *
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectJAdvice
        implements MethodInterceptor {
    private ProceedingJoinPoint joinPoint;
    public MethodBeforeAdviceInterceptor(AspectJAdvice aspectJAdvice) {
        super(aspectJAdvice);
    }

    private void before(Method method, Object[] args, Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        // 前置通知
        before(methodInvocation.getMethod(), methodInvocation.getArguments(),
                methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
