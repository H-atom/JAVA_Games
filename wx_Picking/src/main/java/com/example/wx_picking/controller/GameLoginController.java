package com.example.wx_picking.controller;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wx_picking.dao.NumberProduce;
import com.example.wx_picking.dao.ReturnCode;
import com.example.wx_picking.dao.decideWorD;
import com.example.wx_picking.dao.wuziqi;
import com.example.wx_picking.entity.GameUser;
import com.example.wx_picking.mapper.GameLoginMapper;
import com.example.wx_picking.service.GameLoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/game")
public class GameLoginController {

    @Resource
    private GameLoginService gameLoginService;
    @Resource
    private GameLoginMapper gameLoginMapper;

    ArrayList<String> arrayList=new ArrayList<>();
    @RequestMapping("/login")
    @PostMapping
    public ReturnCode login(@RequestBody GameUser gameUser){
        ReturnCode returnCode=new ReturnCode();
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",gameUser.getUsername());
        GameUser one=gameLoginService.getOne(queryWrapper);
        if (one==null){
            gameLoginService.save(gameUser);
            returnCode.setCode(200);
            returnCode.setObject(gameUser);
            arrayList.add(gameUser.getUsername());
        }else{
            if (Objects.equals(one.getPassword(), gameUser.getPassword())){
                returnCode.setCode(201);
                returnCode.setObject(one);
                arrayList.add(gameUser.getUsername());
            }else {
                returnCode.setCode(500);
            }
        }
        return returnCode;
    }

    NumberProduce numberProduce=new NumberProduce();
    @RequestMapping("/getNumber")
    public ReturnCode getNumber(){
        ReturnCode returnCode=new ReturnCode();
        returnCode.setCode(200);
        returnCode.setObject(numberProduce.produce());
        return returnCode;
    }

    @RequestMapping("decideNumber")
    public ReturnCode decideNumber(@RequestBody decideWorD decideWorD){
        ReturnCode returnCode=new ReturnCode();
        returnCode.setObject(numberProduce.equalNumber(decideWorD.getNumber(),decideWorD.getTrueNumber()));
        returnCode.setCode(200);
       return returnCode;
    }

    @RequestMapping("/checkScore")
    public void checkScore(@RequestBody GameUser gameUser){
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",gameUser.getUsername());
        GameUser one = gameLoginService.getOne(queryWrapper);
        int number=one.getNumber();
        one.setNumber(++number);
        if (gameUser.getMinTime()==0){
            one.setMinTime(gameUser.getMinTime());
        }else {
            if (gameUser.getMinTime()<one.getMinTime()){
                one.setMinTime(gameUser.getMinTime());
            }
        }
        if (gameUser.getMinTime()<=600){
            double x=one.getSumScore();
            x+=10-gameUser.getMinTime()/60;
            DecimalFormat df = new DecimalFormat("0.000");
            one.setSumScore(Double.parseDouble(df.format(x)));
        }
        gameLoginService.update(one,queryWrapper);
    }

    @RequestMapping("/skipScore")
    public void skipScore(@RequestBody GameUser gameUser){
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",gameUser.getUsername());
        GameUser one = gameLoginService.getOne(queryWrapper);
        double x=one.getSumScore();
        x-=(0.5+gameUser.getMinTime()/600);
        DecimalFormat df = new DecimalFormat("0.000");
        one.setSumScore(Double.parseDouble(df.format(x)));
        gameLoginService.update(one,queryWrapper);
    }

    @RequestMapping("/sortByMinTime")
    public List<GameUser> sortByMinTime(){
        return sortBy("min_time",true);
    }

    @RequestMapping("/sortByNumber")
    public List<GameUser> sortByNumber(){
        return sortBy("number",false);
    }

    @RequestMapping("/sortBySumScore")
    public List<GameUser> sortBySumScore(){
        return sortBy("sum_score",false);
    }

    //用户的排行榜
    private List<GameUser> sortBy(String string,boolean bool){
        List<GameUser> gameUser;
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        if (bool){
            queryWrapper.orderByAsc(string);
        }else {
            queryWrapper.orderByDesc(string);
        }
        gameUser=gameLoginMapper.selectList(queryWrapper);
        for (int i = 0; i < gameUser.size(); i++) {
            gameUser.get(i).setRanking(i+1);
        }
        return gameUser;
    }

    @RequestMapping("/userMessage")
    public GameUser userMessage(@RequestBody GameUser gameUser){
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",gameUser.getUsername());
        gameUser=gameLoginService.getOne(queryWrapper);
        return gameUser;
    }

    @RequestMapping("/changeUsername")
    public ReturnCode changeUsername(@RequestBody Map<String,Object> username){
        ReturnCode returnCode=new ReturnCode();
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username.get("username"));
        GameUser gameUser=gameLoginService.getOne(queryWrapper);
        gameUser.setUsername((String) username.get("newName"));
        gameLoginService.update(gameUser,queryWrapper);
        returnCode.setCode(202);
        returnCode.setObject(username.get("newName"));
        return returnCode;
    }

    @RequestMapping("/changePassword")
    public ReturnCode changePassword(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        QueryWrapper<GameUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",message.get("username"));
        GameUser gameUser=gameLoginService.getOne(queryWrapper);
        gameUser.setPassword(message.get("newPassword"));
        gameLoginService.update(gameUser,queryWrapper);
        returnCode.setObject(message.get("newPassword"));
        returnCode.setCode(200);
        return returnCode;
    }


    //五子棋游戏
    Map<String, wuziqi> map=new HashMap<>();//记录棋子的落点问题

    @RequestMapping("/wuziqi")
    public void wuziqi(@RequestBody Map<String,String> message){
        wuziqi wuziqi=new wuziqi();
        wuziqi.setX(message.get("x"));
        wuziqi.setY(message.get("y"));
        wuziqi.setColor(message.get("color"));
        wuziqi.setStart(true);
        wuziqi.setUser(message.get("user"));
        wuziqi.setOpponent(message.get("opponent"));
        map.put(message.get("user"),wuziqi);
    }

    @RequestMapping("/backData")
    public ReturnCode backData(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        if (map.get(message.get("user"))!=null) {
            returnCode.setCode(200);
            returnCode.setObject(map.get(message.get("user")));
            map.remove(message.get("user"));
        }
        return returnCode;
    }


    @RequestMapping("/search")
    public ReturnCode search(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        boolean bool=false;
        for (String s : arrayList) {
            if (s.equals(message.get("user"))) {
                bool = true;
                break;
            }
        }
        if (bool){
            returnCode.setCode(200);
        }else {
            returnCode.setCode(500);
        }
        return returnCode;
    }

    ArrayList<wuziqi> arrayListPK=new ArrayList<>();//记录请求PK的人

    @RequestMapping("/askPK")
    public void askPK(@RequestBody Map<String,String> message){
        addArray(message, arrayListPK);
    }

    @RequestMapping("/askPKUser")
    public ReturnCode askPKUser(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        ArrayList<wuziqi> a=new ArrayList<>();
        boolean bool=false;
        for (com.example.wx_picking.dao.wuziqi wuziqi : arrayListPK) {
            if (wuziqi.getOpponent().equals(message.get("user"))) {
                a.add(wuziqi);
                bool = true;
            }
        }
        if (bool){
            returnCode.setCode(200);
            returnCode.setObject(a);
        }else {
            returnCode.setCode(500);
        }
        return returnCode;
    }

    ArrayList<wuziqi> arrayListAgree=new ArrayList<>();//记录同意PK的人
    @RequestMapping("/isSure")
    public void isSure(@RequestBody Map<String,String> message){
        addArray(message, arrayListAgree);
    }

    private void addArray(@RequestBody Map<String, String> message, ArrayList<wuziqi> arrayListAgree) {
        wuziqi wuziqi=new wuziqi();
        wuziqi.setOpponent(message.get("opponent"));
        wuziqi.setUser(message.get("user"));
        boolean bool=true;
        for (com.example.wx_picking.dao.wuziqi value : arrayListAgree) {
            if (value.equals(wuziqi)) {
                bool = false;
                break;
            }
        }
        if (bool)
            arrayListAgree.add(wuziqi);
    }

    @RequestMapping("/isFind")
    public ReturnCode isFind(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        for (com.example.wx_picking.dao.wuziqi wuziqi : arrayListAgree) {
            if (wuziqi.getOpponent().equals(message.get("user"))
                    && wuziqi.getUser().equals(message.get("opponent"))) {
                returnCode.setCode(200);
                returnCode.setObject(message.get("opponent"));
                arrayListAgree.remove(wuziqi);
                break;
            }else {
                returnCode.setCode(500);
            }
        }
        return returnCode;
    }

    @RequestMapping("/clearPKAgree")
    public void clearQZ(@RequestBody Map<String,String> message){
        for (com.example.wx_picking.dao.wuziqi wuziqi : arrayListAgree) {
            if (wuziqi.getUser().equals(message.get("user"))
                    && wuziqi.getOpponent().equals(message.get("opponent"))) {
                arrayListAgree.remove(wuziqi);
                break;
            }
        }
    }
    @RequestMapping("/clearRequestPK")
    public void clearRequestPK(@RequestBody Map<String,String> message){
        for (com.example.wx_picking.dao.wuziqi wuziqi : arrayListPK) {
            if (wuziqi.getOpponent().equals(message.get("user"))
                    && wuziqi.getUser().equals(message.get("opponent"))) {
                arrayListPK.remove(wuziqi);
                break;
            }
        }
    }
    Map<wuziqi,Integer> requestMap=new HashMap<>();//存放请求重新开始和结束的信号量

    @RequestMapping("/storeRequest")
    public void storeRequest(@RequestBody Map<String,String> message){
        wuziqi wuziqi=new wuziqi();
        wuziqi.setUser(message.get("user"));
        wuziqi.setOpponent(message.get("opponent"));
        requestMap.put(wuziqi, Integer.valueOf(message.get("flag")));
    }

    @RequestMapping("findRequest")
    public ReturnCode findRequest(@RequestBody Map<String,String> message){
        ReturnCode returnCode=new ReturnCode();
        wuziqi wuziqi=new wuziqi();
        wuziqi.setOpponent(message.get("user"));
        wuziqi.setUser(message.get("opponent"));
        if (requestMap.get(wuziqi)!=null){
            returnCode.setCode(200);
            returnCode.setObject(requestMap.get(wuziqi));
            requestMap.remove(wuziqi);
        }
        return returnCode;
    }
}
