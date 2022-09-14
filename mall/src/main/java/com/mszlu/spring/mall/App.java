package com.mszlu.spring.mall;

import com.mszlu.spring.context.annotation.AnnotationConfigApplicationContext;
import com.mszlu.spring.context.support.ClassPathXmlApplicationContext;
import com.mszlu.spring.mall.config.SpringConfig;
import com.mszlu.spring.mall.dao.GoodsDao;
import com.mszlu.spring.mall.dao.impl.GoodsDaoImpl;
import com.mszlu.spring.mall.factory.BeanFactory;
import com.mszlu.spring.mall.pojo.Goods;
import com.mszlu.spring.mall.service.GoodsService;
import com.mszlu.spring.mall.service.impl.GoodsServiceImpl;


/**
 * @author zhongxuetao
 * @Description
 * @date
 **/
public class App {

    public static void main(String[] args) {

//        GoodsDao goodsDao = new GoodsDaoImpl();
//        BeanFactory.RegisterBean("goodsDao", goodsDao);
//        GoodsService goodsService = new GoodsServiceImpl();
//        BeanFactory.RegisterBean("goodsService", goodsService);
//
//        GoodsService goodsService1 = (GoodsService) BeanFactory.getSingleton("goodsService");
//        Goods goodsById = goodsService.findGoodsById(1L);
//        System.out.println(goodsById);
//        System.out.println(goodsService);
//        System.out.println(goodsService1);


//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
//        GoodsService goodsService = (GoodsService) ctx.getBean("goodsService");
//
//        System.out.println(goodsService.findGoodsById(1L));
//        goodsService.test();

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        GoodsService bean = (GoodsService) applicationContext.getBean("goodsService");

//        System.out.println(bean.findGoodsById(1L));
        bean.test();
    }
}
