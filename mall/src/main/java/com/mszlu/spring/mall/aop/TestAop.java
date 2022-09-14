package com.mszlu.spring.mall.aop;

import com.mszlu.spring.context.annotation.Component;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
@Aspect
@Component("testAop")
public class TestAop {

    @Pointcut("execution(public .* com.*.mall.service.impl.*.*(..))")
    public void pt(){

    }

    @Before("pt()")
    public void before(JoinPoint joinPoint){
        System.out.println("-------------before aop------");
    }

    @After("pt()")
    public void after(JoinPoint joinPoint){
        System.out.println("--------------after aop-------");
    }



}
