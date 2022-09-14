package com.mszlu.spring.beans.factory.xml;

/**
 *
 */
public class BeanDefinitionParserDelegate {

    private final XmlReaderContext readerContext;

    public BeanDefinitionParserDelegate(XmlReaderContext readerContext) {
        this.readerContext = readerContext;
    }
}
