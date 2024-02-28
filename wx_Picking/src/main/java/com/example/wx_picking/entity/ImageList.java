package com.example.wx_picking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data

@TableName("img_list")
public class ImageList implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer imgId;
    private String imgSrc;
    private Integer imgForeignId;
}
