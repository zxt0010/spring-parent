package com.mszlu.spring.mall.dao.impl;

import com.mszlu.spring.beans.factory.annotation.Autowired;
import com.mszlu.spring.context.annotation.Component;
import com.mszlu.spring.mall.dao.GoodsDao;
import com.mszlu.spring.mall.pojo.Goods;
import com.mszlu.spring.mall.service.GoodsService;

import java.math.BigDecimal;

/**
 * 商品表的查询服务，和数据库打交道
 */
@Component("goodsDao")
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private GoodsService goodsService;

    public void init(){
        System.out.println("我是dao的init方法");
    }


    public Goods findById(Long id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setGoodsName("我是数据库查出来的商品:"+id);
        goods.setGoodsPrice(BigDecimal.valueOf(100));
        return goods;
    }

    @Override
    public void test() {
        Goods goodsById = goodsService.findGoodsById(1L);
        System.out.println("test:"+goodsById);

    }

    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }
}