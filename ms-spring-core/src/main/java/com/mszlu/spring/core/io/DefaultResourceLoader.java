package com.mszlu.spring.core.io;


import com.mszlu.spring.utils.ClassUtils;


public class DefaultResourceLoader implements ResourceLoader{

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
    }
    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public Resource getResource(String location) {
        //这里先只支持classpath下路径的资源
        return new ClassPathResource(location,getClassLoader());
    }

    @Override
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


}
