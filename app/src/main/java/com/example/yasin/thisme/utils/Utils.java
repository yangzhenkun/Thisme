package com.example.yasin.thisme.utils;

import com.example.yasin.thisme.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Yasin on 2016/2/16.
 */
public class Utils {

    static public String[] HashMapString2StringArray(String data){

        String[] datas;
        datas = data.split("\\{|,|=|\\}");
        return datas;
    }

    static public String Card2JsonString(Card card){
        String jsonString = "";
        JSONObject cardJson = new JSONObject();
        try {
            cardJson.put("cardId",card.getCardId());
            cardJson.put("name",card.getName());
            cardJson.put("phoneNum",card.getPhoneNum());
            cardJson.put("email",card.getEmail());
            cardJson.put("QQ",card.getQQ());
            cardJson.put("weixin",card.getWeixin());
            cardJson.put("miaosu",card.getMiaosu());
            cardJson.put("more",card.getMore());

            jsonString = cardJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonString;
    }

}
