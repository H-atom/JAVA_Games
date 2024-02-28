package com.example.wx_picking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wx_picking.entity.GoodsList;
import com.example.wx_picking.entity.vo.GoodsImage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsListMapper extends BaseMapper<GoodsList> {


//    @Select("SELECT * FROM goods_list a WHERE a.id=#{id}")
//    GoodsList getByIds(Integer id);
}
