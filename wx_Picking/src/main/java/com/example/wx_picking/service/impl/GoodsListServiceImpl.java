package com.example.wx_picking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wx_picking.entity.GoodsList;
import com.example.wx_picking.entity.ImageList;
import com.example.wx_picking.entity.vo.GoodsImage;
import com.example.wx_picking.mapper.GoodsListMapper;
import com.example.wx_picking.mapper.ImageListMapper;
import com.example.wx_picking.service.GoodsListService;
import com.example.wx_picking.service.ImageListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsListServiceImpl extends ServiceImpl<GoodsListMapper, GoodsList> implements GoodsListService {
    @Resource
    private GoodsListMapper goodsListMapper;

    @Resource
    private ImageListMapper imageListMapper;

//    @Override
    public GoodsList getByIds(Integer id){
        QueryWrapper<ImageList> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("img_foreign_id",id);
        GoodsList goodsList=new GoodsList();
        GoodsList goods=goodsListMapper.selectById(id);
        goodsList.setImageLists(imageListMapper.selectList(queryWrapper));
        goodsList.setId(goods.getId());
        goodsList.setSrc(goods.getSrc());
        goodsList.setGoodsName(goods.getGoodsName());
        goodsList.setType(goods.getType());
        goodsList.setIntroduce(goods.getIntroduce());
        goodsList.setPrice(goods.getPrice());
        return goodsList;
    }
}
