package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.core.io.ClassPathResource;
import com.mszlu.spring.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */
public class BeansDtdResolver implements EntityResolver {
    private static final String DTD_EXTENSION = ".dtd";

    private static final String DTD_NAME = "spring-beans";
    //spring逻辑 先从本地的resource中找spring-beans.dtd文件,如果找不到就从网络上读取

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null && systemId.endsWith(DTD_EXTENSION)) {
            int lastPathSeparator = systemId.lastIndexOf('/');
            int dtdNameStart = systemId.indexOf(DTD_NAME, lastPathSeparator);
            if (dtdNameStart != -1) {
                String dtdFile = DTD_NAME + DTD_EXTENSION;
                try {
                     //由于这传递了getClass，所以会去当前的目录下去找spring-beans.dtd文件
                    Resource resource = new ClassPathResource(dtdFile, getClass());
                    InputSource source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    return source;
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }
}