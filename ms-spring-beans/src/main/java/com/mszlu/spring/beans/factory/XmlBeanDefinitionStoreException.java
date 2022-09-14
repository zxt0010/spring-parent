package com.mszlu.spring.beans.factory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 */
public class XmlBeanDefinitionStoreException extends BeanDefinitionStoreException{

    public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause) {
        super(resourceDescription, msg, cause);
    }

    public int getLineNumber() {
        Throwable cause = getCause();
        if (cause instanceof SAXParseException) {
            return ((SAXParseException) cause).getLineNumber();
        }
        return -1;
    }

}