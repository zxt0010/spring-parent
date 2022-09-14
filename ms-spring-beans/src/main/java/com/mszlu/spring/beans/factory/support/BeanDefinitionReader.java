package com.mszlu.spring.beans.factory.support;

import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.core.io.ResourceLoader;

/**
 *
 * 读取配置文件中的内容，将其转换为BeanDefinition
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    /**
     * 资源加载器
     * @return
     */
    ResourceLoader getResourceLoader();

    /**
     * 类加载器
     * @return
     */
    ClassLoader getBeanClassLoader();

    /**
     * 通过指定的resource加载Bean
     * @param resource
     * @return
     */
    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException;
}
