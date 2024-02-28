package com.example.wx_picking.entity;

public class Category {
    int id;
    String name;
    Category_items category_items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category_items getCategory_items() {
        return category_items;
    }

    public void setCategory_items(Category_items category_items) {
        this.category_items = category_items;
    }
}
