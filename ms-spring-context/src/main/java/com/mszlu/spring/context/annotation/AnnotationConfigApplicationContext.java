package com.mszlu.spring.context.annotation;

import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import com.mszlu.spring.beans.factory.NoSuchBeanDefinitionException;
import com.mszlu.spring.beans.factory.config.BeanDefinition;
import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mszlu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mszlu.spring.context.ApplicationContext;
import com.mszlu.spring.context.support.PostProcessorRegistrationDelegate;
import com.mszlu.spring.core.io.DefaultResourceLoader;
import com.mszlu.spring.core.io.Resource;

import java.io.IOException;

/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
public class AnnotationConfigApplicationContext extends DefaultResourceLoader implements AnnotationConfigRegistry, BeanDefinitionRegistry,ApplicationContext {


    private final AnnotatedBeanDefinitionReader reader;

    private final ClassPathBeanDefinitionScanner scanner;

    private DefaultListableBeanFactory beanFactory;

    public AnnotationConfigApplicationContext(){
        this.beanFactory = new DefaultListableBeanFactory();
        reader = new AnnotatedBeanDefinitionReader(this);
        scanner = new ClassPathBeanDefinitionScanner(this);
        this.beanFactory = new DefaultListableBeanFactory();
    }



    public AnnotationConfigApplicationContext(Class<?>... componentClasses){
        //1. 需要把配置类加载进来
        //2. 找到配置类上方的注解ComponentScan 获取需要扫包的路径
        //3. 根据扫包路径 扫描其下的所有class文件, 然后找到哪些加了@Service  @Component等这些注解
        //4. beanFactory进行管理  生成beanDefinition注册, getBean()判断为是否注册为单例,没有则生成实例注册
        this();
        register(componentClasses);
        refresh();

    }

    private void refresh() {
        // Invoke factory processors registered as beans in the context.
        invokeBeanFactoryPostProcessors(beanFactory);
        // Register bean processors that intercept bean creation.
        registerBeanPostProcessors(beanFactory);
    }

    private void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory);
    }

    private void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException {
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
    public Resource[] getResources(String locationPattern) throws IOException {
        return new Resource[0];
    }

    @Override
    public void register(Class<?>... componentClasses) {
        this.reader.register(componentClasses);
    }

    @Override
    public void scan(String... basePackages) {

    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanFactory.getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }
}
