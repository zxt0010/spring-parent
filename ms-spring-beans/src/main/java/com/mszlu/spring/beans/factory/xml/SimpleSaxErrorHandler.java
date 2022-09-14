package com.mszlu.spring.beans.factory.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 */
public class SimpleSaxErrorHandler implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }
}
