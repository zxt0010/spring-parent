package com.mszlu.spring.mall.service;

import com.mszlu.spring.mall.dao.GoodsDao;
import com.mszlu.spring.mall.pojo.Goods;

public interface GoodsService {

    Goods findGoodsById(Long id);

    void setGoodsDao(GoodsDao goodsDao);

    void test();
}