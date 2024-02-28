package com.example.wx_picking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wx_picking.entity.ImageList;
import com.example.wx_picking.mapper.ImageListMapper;
import com.example.wx_picking.service.ImageListService;
import org.springframework.stereotype.Service;

@Service
public class ImageLIstServiceImpl extends ServiceImpl<ImageListMapper, ImageList> implements ImageListService {
}
