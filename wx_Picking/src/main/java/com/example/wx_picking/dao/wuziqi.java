package com.example.wx_picking.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class wuziqi {
    private String x;
    private String y;
    private String color;
    private boolean start;
    private String opponent;
    private String user;
}
