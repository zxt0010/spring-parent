package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

/**
 *
 */
public interface BeanDefinitionDocumentReader {
    /**
     * 将读取到的document解析后，生成beanDefinition并进行注册
     * @param doc
     * @param readerContext
     * @throws BeanDefinitionStoreException
     */
    void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
            throws BeanDefinitionStoreException;
}
