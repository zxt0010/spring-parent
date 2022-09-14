package com.mszlu.spring.context.support;

import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.NoSuchBeanDefinitionException;
import com.mszlu.spring.beans.factory.support.AbstractBeanFactory;
import com.mszlu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mszlu.spring.beans.factory.xml.ResourceEntityResolver;
import com.mszlu.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.mszlu.spring.context.ApplicationContext;
import com.mszlu.spring.core.io.DefaultResourceLoader;
import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.core.io.support.PathMatchingResourcePatternResolver;
import com.mszlu.spring.core.io.support.ResourcePatternResolver;

import java.io.IOException;


public class ClassPathXmlApplicationContext extends DefaultResourceLoader implements ApplicationContext {

    private String[] configLocations;

    private volatile DefaultListableBeanFactory beanFactory;

    /** ResourcePatternResolver used by this context. */
    private final ResourcePatternResolver resourcePatternResolver;


    public ClassPathXmlApplicationContext(String... configLocations){
        this.configLocations = configLocations;
        this.resourcePatternResolver = getResourcePatternResolver();
        refresh();
    }

    protected ResourcePatternResolver getResourcePatternResolver() {
        return new PathMatchingResourcePatternResolver(this);
    }

    private void refresh() {
        //核心，加载所有的Bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //加载所有的bean
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;

        //注册beanprocessor
        registerBeanPostPorcessors(beanFactory);
    }

    private void registerBeanPostPorcessors(DefaultListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory);
    }

    private void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        //xml 读取
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setBeanClassLoader(this.getClassLoader());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        loadBeanDefinitions(beanDefinitionReader);

    }

    private void loadBeanDefinitions(XmlBeanDefinitionReader reader) {

        String[] configLocations = getConfigLocations();

        reader.loadBeanDefinitions(configLocations);
    }

    protected String[] getConfigLocations() {
        return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
    }

    protected String[] getDefaultConfigLocations() {
        return null;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return this.resourcePatternResolver.getResources(locationPattern);
    }
}
