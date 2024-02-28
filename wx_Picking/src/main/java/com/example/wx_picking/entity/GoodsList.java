package com.example.wx_picking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wx_picking.service.ImageListService;
import lombok.Data;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Data
@TableName("goods_list")
public class GoodsList implements Serializable {


    @TableId(type = IdType.AUTO)
    private Integer id;
    private String goodsName;
    private String type;
    private String src;
    private double price;
    private String introduce;

    @TableField(exist = false)
    private List<ImageList> imageLists;

}
