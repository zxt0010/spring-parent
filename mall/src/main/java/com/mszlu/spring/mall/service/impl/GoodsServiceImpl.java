package com.mszlu.spring.mall.service.impl;

import com.mszlu.spring.beans.factory.annotation.Autowired;
import com.mszlu.spring.context.stereotype.Service;
import com.mszlu.spring.mall.dao.GoodsDao;
import com.mszlu.spring.mall.factory.BeanFactory;
import com.mszlu.spring.mall.pojo.Goods;
import com.mszlu.spring.mall.service.GoodsService;

/**
 *商品服务，对外提供商品相关的业务实现
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;


    private String test;

    public GoodsServiceImpl(){

    }

    public Goods findGoodsById(Long id) {
        System.out.println("find 方法执行");
        return goodsDao.findById(id);
    }

    @Override
    public void setGoodsDao(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    @Override
    public void test() {
        goodsDao.test();
    }

    public void setTest(String test) {
        this.test = test;
    }
}