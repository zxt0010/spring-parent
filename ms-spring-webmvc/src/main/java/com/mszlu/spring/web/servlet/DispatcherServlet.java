package com.mszlu.spring.web.servlet;

import com.alibaba.fastjson.JSON;
import com.mszlu.spring.context.ApplicationContext;
import com.mszlu.spring.context.annotation.AnnotationConfigApplicationContext;
import com.mszlu.spring.web.handler.HandlerMethodMapping;
import com.mszlu.spring.web.method.HandlerMethod;
import com.mszlu.spring.web.servlet.mvc.method.HandlerMethodAdapter;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;

    private HandlerAdapter handlerAdapter;


    public DispatcherServlet(){
        super();
    }

    /**
     * 初始化，加载配置文件
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            ServletContext servletContext = config.getServletContext();
            Class<?> springConfig = Class.forName(servletContext.getInitParameter("springConfig"));
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(springConfig);
            initStrategies(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }


    /**
     * 执行业务处理
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            //匹配对应方法
            doDispatch(req,resp);
        }catch(Exception e){
            resp.getWriter().write("500 Exception,Details:\r\n"
                    + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                    .replaceAll(",\\s", "\r\n"));
        }
    }

    private void doDispatch(HttpServletRequest req,HttpServletResponse resp)
            throws Exception{
        HandlerMethod handler = handlerMapping.getHandler(req);
        if(null == handler){
            resp.getWriter().write("404 Not Found");
            return;
        }
        try {
            ModelAndView mv = handlerAdapter.handle(req,resp,handler);
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write(JSON.toJSONString(mv.getModel()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //初始化策略
    private void initStrategies(ApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //初始化Handler映射处理器
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        //FlashMap管理器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(ApplicationContext context) {
    }

    private void initViewResolvers(ApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapter = new HandlerMethodAdapter();
    }

    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMapping = new HandlerMethodMapping(context);
    }

    private void initThemeResolver(ApplicationContext context) {
    }

    private void initLocaleResolver(ApplicationContext context) {
    }

    private void initMultipartResolver(ApplicationContext context) {
    }
}
