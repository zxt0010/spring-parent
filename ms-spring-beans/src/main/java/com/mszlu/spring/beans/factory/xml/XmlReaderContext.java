package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.core.io.ResourceLoader;

/**
 * 与XmlBeanDefinitionReader一起使用
 *
 */
public class XmlReaderContext extends ReaderContext{

    private final XmlBeanDefinitionReader reader;

    public XmlReaderContext(Resource resource, XmlBeanDefinitionReader reader) {
        super(resource);
        this.reader = reader;
    }
    /**
     * Return the XML bean definition reader in use.
     */
    public final XmlBeanDefinitionReader getReader() {
        return this.reader;
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.reader.getRegistry();
    }

    public final ResourceLoader getResourceLoader() {
        return this.reader.getResourceLoader();
    }

    public final ClassLoader getBeanClassLoader() {
        return this.reader.getBeanClassLoader();
    }


}