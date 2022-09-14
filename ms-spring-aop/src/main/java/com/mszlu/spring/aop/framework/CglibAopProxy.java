package com.mszlu.spring.aop.framework;

import com.mszlu.spring.aop.framework.AdvisedSupport;
import com.mszlu.spring.aop.framework.AopProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.interceptor.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 *
 */
public class CglibAopProxy implements AopProxy, Serializable, MethodInterceptor {


    private final AdvisedSupport advised;
    private final Enhancer enhancer = new Enhancer();

    public CglibAopProxy(AdvisedSupport config) throws Exception {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Class<?> rootClass = this.advised.getTargetClass();
        Class<?> proxySuperClass = rootClass;
        if (rootClass.getName().contains("$$")) {
            proxySuperClass = rootClass.getSuperclass();
        }
        enhancer.setSuperclass(proxySuperClass);
        enhancer.setCallback(this);
        enhancer.setClassLoader(classLoader);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.getName().equals("hashCode") || method.getName().equals("toString") || method.getName().equals("equals")){
            return proxy.invokeSuper(obj, args);
        }
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(
                method,this.advised.getTargetClass());
        //如果没有可以应用到此方法的通知(Interceptor)，直接反射调用
        if(null==chain){
            return proxy.invokeSuper(obj, args);
        }
        MethodInvocation invocation = new ReflectiveMethodInvocation(
                proxy,advised.getTarget(),method,args,this.advised.getTargetClass(),chain);
        return invocation.proceed();
    }
}
