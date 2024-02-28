package com.example.wx_picking.controller;

import com.example.wx_picking.entity.swiper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/Home")
public class HomeController {

    @RequestMapping("/swiper")
    public ArrayList<swiper> swiper(){
        ArrayList<swiper> arrayList=new ArrayList<>();
        swiper swiper=new swiper();
        swiper.setId(0);
        swiper.setMess("第一个照片");
        swiper.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1114%2F0Z320111T2%2F200Z3111T2-1-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651480120&t=ccd0bedc4c820cd917c69ccdb8a5d881");
        swiper swiper1=new swiper();
        swiper1.setId(1);
        swiper1.setMess("第二个照片");
        swiper1.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F0F420110430%2F200F4110430-6-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651499222&t=301c08e8d8f576831db9d67f56858154");
        swiper swiper2=new swiper();
        swiper2.setId(2);
        swiper2.setMess("第三张照片");
        swiper2.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2Ftp01%2F1ZZP044191A4-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651499309&t=19681fd4b04964c04f94df5cdeb97d6a");
        arrayList.add(swiper);
        arrayList.add(swiper1);
        arrayList.add(swiper2);
        return arrayList;
    }

    @RequestMapping("/cateUp")
    public ArrayList<swiper> cateUp(){
        ArrayList<swiper> arrayList=new ArrayList<>();
        String[] mess={"书籍","食品","电器","车辆","衣物"};
        String[] url={"http://localhost:8888/image/book.png","http://localhost:8888/image/food.png","http://localhost:8888/image/ele_equipment.png",
                "http://localhost:8888/image/bicycle.png","http://localhost:8888/image/clothes.png"};
        for (int i = 0; i < mess.length; i++) {
            swiper swiper=new swiper();
            swiper.setId(i);
            swiper.setMess(mess[i]);
            swiper.setUrl(url[i]);
            arrayList.add(swiper);
        }
        return arrayList;
    }

    @RequestMapping("/cateDown")
    public ArrayList<swiper> cateDown(){
        ArrayList<swiper> arrayList=new ArrayList<>();
        String[] mess={"日用品","会员","拼团","帮捎","更多"};
        String[] url={"http://localhost:8888/image/daily_thing.png","http://localhost:8888/image/vip.png","http://localhost:8888/image/collage.png",
                "http://localhost:8888/image/express.png","http://localhost:8888/image/sort.png"};
        for (int i = 0; i < mess.length; i++) {
            swiper swiper=new swiper();
            swiper.setId(i);
            swiper.setMess(mess[i]);
            swiper.setUrl(url[i]);
            arrayList.add(swiper);
        }
        return arrayList;
    }
}
