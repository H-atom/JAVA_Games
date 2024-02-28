package com.example.wx_picking.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
//@NoArgsConstructor
public class ReturnCode {
    private Object object;
    private int code;
}
