package com.example.wx_picking.controller;

import com.example.wx_picking.entity.Category;
import com.example.wx_picking.entity.Category_items;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/Category")
public class CategoryController {

    @RequestMapping("/Items")
    public ArrayList<Category> Items(){
        ArrayList<Category> arrayList=new ArrayList<>();
        String[] name={"书籍","食品","车辆","衣物","日用品","会员","拼团","帮捎"};
        Category_items category_items=new Category_items();
        category_items.setId(0);
        category_items.setName("高等数学");
        Category category=new Category();
        category.setId(0);
        category.setName("书籍");
        category.setCategory_items(category_items);
        arrayList.add(category);
        return arrayList;
    }
}
