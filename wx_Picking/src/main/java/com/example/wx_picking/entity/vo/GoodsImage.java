package com.example.wx_picking.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsImage implements Serializable {

    private Integer id;
    private String goodsName;
    private String type;
    private String src;
    private double price;
    private String introduce;
    private Integer imgId;
    private String imgSrc;
    private Integer imgForeignId;
}
