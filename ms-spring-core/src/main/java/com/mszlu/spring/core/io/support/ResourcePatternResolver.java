package com.mszlu.spring.core.io.support;

import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.core.io.ResourceLoader;

import java.io.IOException;

/**
 * 用于解析资源文件的策略接口
 *
 */
public interface ResourcePatternResolver extends ResourceLoader {

    /**
     * 在所有根目录下搜索文件的伪URL的前缀
     * 与ResourceLoader中classpath不同的地方在于，此前缀会在所有的JAR包的根目录下搜索指定文件。
     */
    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    /**
     * 返回指定路径下所有的资源对象。
     * 返回的对象集合应该有Set的语义，也就是说，对于同一个资源，只应该返回一个资源对象
     */
    Resource[] getResources(String locationPattern) throws IOException;

}