package com.example.yasin.thisme.utils;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.yasin.thisme.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasin on 2016/4/28.
 */
public class MyTest extends InstrumentationTestCase{
    public void test() throws Exception{
        String json1 = "{\"allcard\":[{\"cardid\":\"123\",\"inf\":[{\"name\":\"yasin\",\"phoneNum\":\"1234567890\",\"shuxing\":\"1\",\"email\":\"1334036616@qq.com\",\"miaosu\":\"zi\",\"more\":\"{a=a}\",\"qq\":\"133403\",\"weixin\":\"yasin\"}]},{\"cardid\":\"124\",\"inf\":[{\"name\":\"yasin1\",\"phoneNum\":\"1234567891\",\"shuxing\":\"2\",\"email\":\"1334036616@qq.com1\",\"miaosu\":\"zi1\",\"more\":\"{b=b}\",\"qq\":\"1334031\",\"weixin\":\"yasin1\"}]}]}";
        String json = "{\"allcard\":[{\"name\":\"yasin\"},{\"name\":\"yzk\"}]}";
        String json2 = "{\"allcard\":[{\"name\":\"yasin\",\"card\":[{\"num\":\"123\"}]},{\"name\":\"yzk\",\"card\":[{\"num\":\"456\"}]}]}";
        List<Card> list = new ArrayList<Card>();
        list = Utils.JsonArray2CardList(json1);
        for(int i=0;i<list.size();i++){
            Log.e("card",list.get(i).toString());
        }
    }
}
