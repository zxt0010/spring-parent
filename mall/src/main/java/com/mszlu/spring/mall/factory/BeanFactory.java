package com.mszlu.spring.mall.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/

public class BeanFactory {
    private final  static Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public static Object getSingleton(String beanName){
        Object object = singletonObjects.get(beanName);
        return object;
    }

    public static void RegisterBean(String beanName, Object singletonObject){
        if(beanName == null){
            throw  new IllegalArgumentException("beanName不能为空");
        }
        if(singletonObject == null){
            throw  new IllegalArgumentException("object不能为空");
        }
        synchronized (singletonObjects){
            Object oldObject = singletonObjects.get(beanName);
            if(oldObject != null){
                throw  new IllegalStateException(
                        "不能注册 object [" + singletonObject+"]在这个beanName【"+beanName+"】下，" +
                                "因为其已经有对象【"+oldObject+"】绑定了");
            }
            singletonObjects.put(beanName, singletonObject);
        }

    }

}
