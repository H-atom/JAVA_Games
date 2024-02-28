package com.example.wx_picking.dao;

import java.util.ArrayList;

public class NumberProduce {

    public String produce(){
        String string=null;
        ArrayList<Integer> arrayList=new ArrayList<>();
        StringBuilder stringBuilder=new StringBuilder();
        while (true){
            boolean ax=true;
            int x=(int)(Math.random()*10);
//            if (arrayList.size()==0){
//                arrayList.add(x);
//            }
            for (Integer integer : arrayList) {
                if (x == integer) {
                    ax = false;
                    break;
                }
            }
            if (ax){
                arrayList.add(x);
                stringBuilder.append(x);
            }
            if (arrayList.size()==4){
                break;
            }
        }
        string= String.valueOf(stringBuilder);
        return string;
    }

    public String equalNumber(String str,String string){
        String[] number=str.split("");
        String[] strings=string.split("");
        int countA=0,countB=0;
        for (int i = 0; i < number.length; i++) {
            if (number[i].equals(strings[i])){
                countA++;
            }
            for (int j = 0; j < 4; j++) {
                if (number[i].equals(strings[j])){
                    countB++;
                }
            }

        }
        return countA+"A"+countB+"B";
    }
    public static void main(String[] args) {
        NumberProduce numberProduce=new NumberProduce();
        numberProduce.produce();
       // System.out.println(numberProduce.equalNumber("1232"));
    }
}
