package com.example.yasin.thisme.model;

import java.util.HashMap;

/**
 * Created by Yasin on 2016/1/30.
 */
public class Card {
    private String name,phoneNum,QQ,Weixin,Email,shuxing;
    private HashMap<String,String> more;

    public Card(String shuxing,String phoneNum, String QQ, String weixin, String email, HashMap<String, String> more, String name) {
        this.shuxing = shuxing;
        this.phoneNum = phoneNum;
        this.QQ = QQ;
        Weixin = weixin;
        Email = email;
        this.more = more;
        this.name = name;
    }
    public Card(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getWeixin() {
        return Weixin;
    }

    public void setWeixin(String weixin) {
        Weixin = weixin;
    }

    public String getShuxing() {
        return shuxing;
    }

    public void setShuxing(String shuxing) {
        this.shuxing = shuxing;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public HashMap<String, String> getMore() {
        return more;
    }

    public void setMore(HashMap<String, String> more) {
        this.more = more;
    }
}
