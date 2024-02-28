package com.example.wx_picking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wx_picking.entity.GoodsList;
import com.example.wx_picking.entity.ImageList;
import com.example.wx_picking.entity.vo.GoodsImage;
import com.example.wx_picking.mapper.GoodsListMapper;
import com.example.wx_picking.mapper.ImageListMapper;
import com.example.wx_picking.service.GoodsListService;
import com.example.wx_picking.service.ImageListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/Goods")
public class GoodsController {

    @Resource
    private GoodsListService goodListService;

    @Resource
    private GoodsListMapper goodsListMapper;



    @RequestMapping("/goodsList")
    public IPage<GoodsList> goodsList(@RequestParam(required = false) String query,
                                       @RequestParam String type,
                                       @RequestParam(name = "pagenum",defaultValue = "1") int pageNum,
                                       @RequestParam(name = "pagesize",defaultValue = "10") int pageSize){
        QueryWrapper<GoodsList> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type",type);
        IPage<GoodsList> iPage=new Page<>(pageNum,pageSize);
        return goodsListMapper.selectPage(iPage,queryWrapper);
    }

    @RequestMapping("/goodsDetail")
    public GoodsList goodsDetail(@RequestParam(name = "id") Integer goodsId){
        return goodListService.getByIds(goodsId);
    }

}
