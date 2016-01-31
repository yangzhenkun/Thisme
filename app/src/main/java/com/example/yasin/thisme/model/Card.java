package com.example.yasin.thisme.model;

import java.util.HashMap;

/**
 * Created by Yasin on 2016/1/30.
 * Card的shuxing为1时表示为用户自己创建的名片，为2时表示用户添加的别人的名片
 * miaosu为card的描述
 */
public class Card {
    private String name,phoneNum,QQ,Weixin,Email,shuxing,miaosu;
    int cardId;
    private String more;

    public String getMiaosu() {
        return miaosu;
    }

    public void setMiaosu(String miaosu) {
        this.miaosu = miaosu;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public Card(int cardId,String miaosu,String shuxing,String phoneNum, String QQ, String weixin, String email, String more, String name) {
        this.shuxing = shuxing;
        this.miaosu = miaosu;
        this.cardId = cardId;
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

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
