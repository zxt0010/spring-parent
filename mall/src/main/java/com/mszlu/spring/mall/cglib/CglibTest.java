package com.mszlu.spring.mall.cglib;

import com.mszlu.spring.mall.proxy.Hello;
import com.mszlu.spring.mall.proxy.RealHello;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
public class CglibTest {

    public static void main(String[] args) {
        Hello hello = (Hello) new CglibProxyHello().getProxy(RealHello.class);
        hello.sayHello();
    }
}
