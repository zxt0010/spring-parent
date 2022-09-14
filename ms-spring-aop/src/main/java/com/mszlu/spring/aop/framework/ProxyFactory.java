package com.mszlu.spring.aop.framework;

import java.lang.reflect.Proxy;

/**
 *
 */
public class ProxyFactory extends AdvisedSupport{

    public Object getProxy() throws Exception {
        return createAopProxy().getProxy();
    }

    public Object getProxy(ClassLoader classLoader)  throws Exception{
        return createAopProxy().getProxy(classLoader);
    }

    private synchronized AopProxy createAopProxy() throws Exception{
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            throw new Exception("没有代理目标类");
        }
        if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
            return new CglibAopProxy(this);
        }
        return new CglibAopProxy(this);
    }
}
