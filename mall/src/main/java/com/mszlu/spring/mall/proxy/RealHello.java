package com.mszlu.spring.mall.proxy;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
public class RealHello implements Hello{


    @Override
    public void sayHello() {
        System.out.println("say hello!");
    }
}
