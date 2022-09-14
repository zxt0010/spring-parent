package com.mszlu.spring.core.io;

/**
 *
 */
public interface ResourceLoader {
    /**
     * 通过指定的路径返回Resource
     * 必须支持完全限定的URL，例如“file:C:/test.dat”。
     *
     * 必须支持类路径伪URL，例如“classpath:test.dat”。
     *
     * 应支持相对文件路径，例如“WEB-INF/test.dat”。
     *
     * 使用的时候 还需要调用resource的exist方法判断是否存在
     * @param location
     * @return
     */
    Resource getResource(String location);

    /**
     * 返回此ResourceLoader的类加载器
     * @return
     */
    ClassLoader getClassLoader();
}