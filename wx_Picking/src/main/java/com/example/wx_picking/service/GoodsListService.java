package com.example.wx_picking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wx_picking.entity.GoodsList;
import com.example.wx_picking.entity.vo.GoodsImage;

import java.util.List;

public interface GoodsListService extends IService<GoodsList> {
    public GoodsList getByIds(Integer id);
}
