package com.mszlu.spring.web.handler;

import com.mszlu.spring.aop.framework.AopProxyUtils;
import com.mszlu.spring.context.ApplicationContext;
import com.mszlu.spring.context.stereotype.Controller;
import com.mszlu.spring.web.bind.annotation.RequestMapping;
import com.mszlu.spring.web.method.HandlerMethod;
import com.mszlu.spring.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;




public class HandlerMethodMapping implements HandlerMapping {

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public HandlerMethodMapping(ApplicationContext context) {
        initHandlerMethod(context);
    }


    @Override
    public final HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        return this.mappingRegistry.getHandlerMethod(url);
    }


    private void initHandlerMethod(ApplicationContext context) {
        String [] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {

                Object bean = context.getBean(beanName);
                Object target = AopProxyUtils.getTargetObject(bean);

                Class<?> clazz = target.getClass();

                //只对加了@Controller注解的类进行初始化
                if(!clazz.isAnnotationPresent(Controller.class)){
                    continue;
                }

                String baseUrl = "";
                //获取Controller的url配置
                if(clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping =
                            clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                //获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {

                    //没有加RequestMapping注解的直接忽略
                    if(!method.isAnnotationPresent(RequestMapping.class)){ continue; }

                    //映射URL
                    RequestMapping requestMapping =
                            method.getAnnotation(RequestMapping.class);

                    //  /demo/query 字符串处理
                    //  (//demo//query)
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().
                            replaceAll("\\*",".*")).replaceAll("/+", "/");

                    this.mappingRegistry.register(regex,target,method);
                    System.out.println("Mapped " + regex + "," + method);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private HandlerMethod createHandlerMethod(Object handler, Method method) {
        return new HandlerMethod(handler, method);
    }

    class MappingRegistry {

        private final Map<String, MappingRegistration<String>> registry =
                new HashMap<>();

        private final Map<String, HandlerMethod> mappingLookup = new LinkedHashMap<>();

        public Map<String, HandlerMethod> getMappings() {
            return this.mappingLookup;
        }

        public HandlerMethod getHandlerMethod(String mappings) {
            return this.mappingLookup.get(mappings);
        }

        public void register(String mapping, Object handler, Method method) {
            HandlerMethod handlerMethod = createHandlerMethod(handler, method);
            this.mappingLookup.put(mapping, handlerMethod);
        }
    }

    private static class MappingRegistration<T> {

        private final T mapping;

        private final HandlerMethod handlerMethod;

        public MappingRegistration(T mapping, HandlerMethod handlerMethod) {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
        }

        public T getMapping() {
            return this.mapping;
        }

        public HandlerMethod getHandlerMethod() {
            return this.handlerMethod;
        }
    }
}
