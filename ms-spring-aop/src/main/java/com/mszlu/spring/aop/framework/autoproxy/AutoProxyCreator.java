package com.mszlu.spring.aop.framework.autoproxy;

import com.mszlu.spring.aop.aspectj.AspectJAdvice;
import com.mszlu.spring.aop.framework.AdvisedSupport;
import com.mszlu.spring.aop.framework.ProxyFactory;
import com.mszlu.spring.aop.framework.adapter.AfterReturningAdviceInterceptor;
import com.mszlu.spring.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import com.mszlu.spring.beans.BeanWrapper;
import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.BeanFactory;
import com.mszlu.spring.beans.factory.config.BeanPostProcessor;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class AutoProxyCreator implements BeanPostProcessor {

    private final Map<String,List<AspectJAdvice>> aspectMethods;

    private final List<String> patterns;


    public AutoProxyCreator(Map<String, List<AspectJAdvice>> aspectMethods,
                            List<String> patterns) {
        this.aspectMethods = aspectMethods;
        this.patterns = patterns;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName,
                                                  BeanFactory beanFactory)
            throws Exception {
        if (bean != null) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.setTargetClass(bean.getClass());
            proxyFactory.setMethodCache(
                    getMethodCathe(proxyFactory,beanFactory, Before.class));
            proxyFactory.setMethodCache(
                    getMethodCathe(proxyFactory,beanFactory,After.class));
            Object proxy = proxyFactory.getProxy();
            BeanWrapper beanWrapper = new BeanWrapper();
            beanWrapper.setBean(proxy);
            beanWrapper.setAop(!proxyFactory.getMethodCache().isEmpty());
            return beanWrapper;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName,
                                                 BeanFactory beanFactory)
            throws Exception {
//        if (bean != null) {
//            ProxyFactory proxyFactory = new ProxyFactory();
//            proxyFactory.setTarget(bean);
//            proxyFactory.setTargetClass(bean.getClass());
//            proxyFactory.setMethodCache(
//                    getMethodCathe(proxyFactory,beanFactory,After.class));
//            Object proxy = proxyFactory.getProxy();
//            return proxy;
//        }
        return bean;
    }

    private List<Object> getAdvices(
            String p,BeanFactory beanFactory,Class<? extends Annotation> annotationClass)
            throws Exception{
        List<Object> advices = new ArrayList<>();
        List<AspectJAdvice> aspectJAdviceList = aspectMethods.get(p);
        for (AspectJAdvice aspectJAdvice:aspectJAdviceList) {
            //注入
            if(aspectJAdvice.getAspectTarget() instanceof String){
                aspectJAdvice.setAspectTarget(
                        beanFactory.getBean((String)aspectJAdvice.getAspectTarget()));
            }
            if(aspectJAdvice.getAspectMethod().isAnnotationPresent(Before.class)){
                advices.add(new MethodBeforeAdviceInterceptor(aspectJAdvice));
            }else if(aspectJAdvice.getAspectMethod().isAnnotationPresent(After.class)){
                advices.add(new AfterReturningAdviceInterceptor(aspectJAdvice));
            }
        }
        return advices;
    }

    private Map<Method, List<Object>> getMethodCathe(
            AdvisedSupport advisedSupport, BeanFactory beanFactory,
            Class<? extends Annotation> annotationClass) throws Exception{
        Map<Method, List<Object>> methodCache = new HashMap<>();
        Method[] methods=advisedSupport.getTarget().getClass().getMethods();
        List<Object> advices;
        Matcher matcher;
        String methodString;
        Pattern pattern;
        for (Method method:methods) {
            methodString = method.toString();
            if(methodString.contains("throws")){
                methodString =
                        methodString.substring(0,methodString.lastIndexOf("throws")).trim();
            }
            //判断所有的Aspect中的方法上的正则查看是否匹配
            for (String p:patterns){
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(methodString);
                if(matcher.matches()){
                    advices = getAdvices(p,beanFactory,annotationClass);
                    methodCache.put(method,advices);
                    break;
                }
            }
        }
        return methodCache;
    }
}
