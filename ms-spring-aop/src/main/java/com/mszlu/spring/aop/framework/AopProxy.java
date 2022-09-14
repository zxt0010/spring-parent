package com.mszlu.spring.aop.framework;

/**
 */
public interface AopProxy {
    /**
     *使用默认的类加载器生成代理对象，默认的类加载器通常是当前线程
     *的上下文类加载器，可通过Thread#getContextClassLoader()获得
     */
    Object getProxy();
    /**
     * 使用指定的类加载器创建代理对象，通常用于比较低级别的代理对象
     * 创建
     */
    Object getProxy(ClassLoader classLoader);
}
