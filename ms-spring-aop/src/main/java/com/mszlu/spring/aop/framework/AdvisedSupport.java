package com.mszlu.spring.aop.framework;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * AOP代理配置
 */
public class AdvisedSupport{

    private Class<?> targetClass;

    private Object target;

    private Pattern pointCutClassPattern;

    /** 缓存方法为键，调用链列表为值 */
    private transient Map<Method, List<Object>> methodCache;


    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
            Method method,Class<?> targetClass)
            throws Exception {
        List<Object> cached = this.methodCache.get(method);
        if (cached == null) {
            Method m =
                    targetClass.getMethod(method.getName(),method.getParameterTypes());
            cached = methodCache.get(m);
            this.methodCache.put(method, cached);
        }
        return cached;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Pattern getPointCutClassPattern() {
        return pointCutClassPattern;
    }

    public void setPointCutClassPattern(Pattern pointCutClassPattern) {
        this.pointCutClassPattern = pointCutClassPattern;
    }

    public Map<Method, List<Object>> getMethodCache() {
        return methodCache;
    }

    public void setMethodCache(Map<Method, List<Object>> methodCache) {
        this.methodCache = methodCache;
    }
}
