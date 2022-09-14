package com.mszlu.spring.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * 文档加载器
 *
 */
public interface DocumentLoader {
    /**
     * 根据输入源获取Document对象
     * @param inputSource
     * @param entityResolver
     * @param errorHandler
     * @param validationMode
     * @param namespaceAware
     * @return
     * @throws Exception
     */
    Document loadDocument(
            InputSource inputSource, EntityResolver entityResolver,ErrorHandler errorHandler,int validationMode, boolean namespaceAware)
            throws Exception;
}
