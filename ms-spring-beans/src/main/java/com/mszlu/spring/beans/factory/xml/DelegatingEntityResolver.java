package com.mszlu.spring.beans.factory.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 *
 */
public class DelegatingEntityResolver implements EntityResolver {

    /** Suffix for DTD files. */
    public static final String DTD_SUFFIX = ".dtd";

    /** Suffix for schema definition files. */
    public static final String XSD_SUFFIX = ".xsd";

//    @Override
//    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//        return null;
//    }


    private final EntityResolver dtdResolver;
//
    private final EntityResolver schemaResolver;
//
//
    public DelegatingEntityResolver(ClassLoader classLoader) {
        this.dtdResolver = new BeansDtdResolver();
        this.schemaResolver = new PluggableSchemaResolver(classLoader);
    }
//
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null) {
            if (systemId.endsWith(DTD_SUFFIX)) {
                return this.dtdResolver.resolveEntity(publicId, systemId);
            }
            else if (systemId.endsWith(XSD_SUFFIX)) {
                return this.schemaResolver.resolveEntity(publicId, systemId);
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }
}
