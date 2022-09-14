package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.core.io.Resource;

/**
 * 在bean定义读取过程中传递的上下文，封装了所有相关的配置和状态。
 *
 */
public class ReaderContext {

    private final Resource resource;

    public ReaderContext(Resource resource) {
        this.resource = resource;
    }

    public final Resource getResource() {
        return this.resource;
    }
}