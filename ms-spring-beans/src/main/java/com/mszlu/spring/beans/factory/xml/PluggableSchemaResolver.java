package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.core.io.ClassPathResource;
import com.mszlu.spring.core.io.Resource;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class PluggableSchemaResolver implements EntityResolver {

    public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";

    private final ClassLoader classLoader;

    private final String schemaMappingsLocation;

    private volatile Map<String, String> schemaMappings;


    public PluggableSchemaResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
    }

    public PluggableSchemaResolver(ClassLoader classLoader, String schemaMappingsLocation) {
        this.classLoader = classLoader;
        this.schemaMappingsLocation = schemaMappingsLocation;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null) {
            //直接读取本地的xsd文件
            //实际上在META-INF下有个spring.schema文件，里面定义了各个版本的xsd文件，读取到就从本地读取，读取不到就从网络读取
            String resourceLocation = "spring-beans.xsd";
//            String resourceLocation = getSchemaMappings().get(systemId);
//            if (resourceLocation == null && systemId.startsWith("https:")) {
//                // Retrieve canonical http schema mapping even for https declaration
//                resourceLocation = getSchemaMappings().get("http:" + systemId.substring(6));
//            }
            if (resourceLocation != null) {
                Resource resource = new ClassPathResource(resourceLocation, getClass());
                try {
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
//
//    private Map<String, String> getSchemaMappings() {
//        Map<String, String> schemaMappings = this.schemaMappings;
//        if (schemaMappings == null) {
//            synchronized (this) {
//                schemaMappings = this.schemaMappings;
//                if (schemaMappings == null) {
//                    try {
//                        Properties mappings =
//                                PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
//                        schemaMappings = new ConcurrentHashMap<>(mappings.size());
//                        CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
//                        this.schemaMappings = schemaMappings;
//                    }
//                    catch (IOException ex) {
//                        throw new IllegalStateException(
//                                "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
//                    }
//                }
//            }
//        }
//        return schemaMappings;
//    }

}
