package com.example.wx_picking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wx_picking.entity.GameUser;
import com.example.wx_picking.mapper.GameLoginMapper;
import com.example.wx_picking.service.GameLoginService;
import org.springframework.stereotype.Service;

@Service
public class GameLoginServiceImpl extends ServiceImpl<GameLoginMapper, GameUser> implements GameLoginService {
}
