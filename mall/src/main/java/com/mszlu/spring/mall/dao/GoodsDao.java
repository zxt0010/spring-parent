package com.mszlu.spring.mall.dao;

import com.mszlu.spring.mall.pojo.Goods;

public interface GoodsDao {

    Goods findById(Long id);

    void test();

}
