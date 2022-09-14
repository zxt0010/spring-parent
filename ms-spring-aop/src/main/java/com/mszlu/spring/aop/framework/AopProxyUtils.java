package com.mszlu.spring.aop.framework;

import com.mszlu.spring.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public abstract class AopProxyUtils {

    public static Object getTargetObject(Object proxy) throws Exception{
        if(!isAopProxy(proxy)){ return proxy; }
        return getProxyTargetObject(proxy);
    }

    private static boolean isAopProxy(Object object){
        return Proxy.isProxyClass(object.getClass());
    }


    private static Object getProxyTargetObject(Object proxy) throws Exception{
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        AdvisedSupport advisedSupport = (AdvisedSupport)advised.get(aopProxy);
        return getTargetObject(advisedSupport.getTarget());
    }


}