package com.mszlu.spring.mall.proxy;

import java.lang.reflect.Proxy;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
public class TestJdkProxyHello {
    public static void main(String[] args) {
        Hello hello = (Hello) Proxy.newProxyInstance(RealHello.class.getClassLoader(),
                RealHello.class.getInterfaces(), new JdkProxyHello(new RealHello()));
        hello.sayHello();
    }
}
