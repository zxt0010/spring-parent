package com.mszlu.spring.web.servlet;

import com.mszlu.spring.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 映射处理器

 */
public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
