package com.mszlu.spring.mall.controller;

import com.mszlu.spring.beans.factory.annotation.Autowired;
import com.mszlu.spring.context.stereotype.Controller;
import com.mszlu.spring.mall.pojo.Goods;
import com.mszlu.spring.mall.service.GoodsService;
import com.mszlu.spring.ui.ModelMap;
import com.mszlu.spring.web.bind.annotation.RequestMapping;
import com.mszlu.spring.web.bind.annotation.RequestParam;
import com.mszlu.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author B站：码神之路
 */
@Controller("helloController")
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private GoodsService goodsService;


    @RequestMapping("/find")
    public ModelAndView findGoods(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        Goods goodsById = goodsService.findGoodsById(Long.parseLong(id));
        ModelAndView modelAndView = new ModelAndView();
        ModelMap model = new ModelMap();
        model.put("goods",goodsById);
        modelAndView.setModel(model);
        return modelAndView;
    }
}
