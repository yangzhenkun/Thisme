package com.example.yasin.thisme.utils;

import android.util.Log;

import com.example.yasin.thisme.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yasin on 2016/2/16.
 */
public class Utils {

   // public static final String baseUrl = "http://10.1.104.6:16666/ccard/";
   public static final String baseUrl = "http://115.159.73.94/ccard/";
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
            cardJson.put("shuxing",card.getShuxing());
            cardJson.put("more",card.getMore());

            jsonString = cardJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonString;
    }

    static public List<Card> JsonArray2CardList(String json) throws JSONException {
        List<Card> list = new ArrayList<Card>();
        Card mCard;
        //JSONObject jsonObject = new JSONObject(json);
       // JSONArray jsonArray = jsonObject.getJSONArray("allcard");
        JSONArray jsonArray = new JSONArray(json);
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            mCard = new Card();
            mCard.setCardIdFromS(jsonObject.getString("cid"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("inf"));
                mCard.setQQ(jsonObject1.getString("QQ"));
                mCard.setWeixin(jsonObject1.getString("weixin"));
                mCard.setMore(jsonObject1.getString("more"));
                mCard.setMiaosu(jsonObject1.getString("miaosu"));
                mCard.setEmail(jsonObject1.getString("email"));
                mCard.setName(jsonObject1.getString("name"));
                mCard.setPhoneNum(jsonObject1.getString("phoneNum"));
                mCard.setShuxing(jsonObject1.getString("shuxing"));
                list.add(mCard);
                mCard = null;

        }

        return list;
    }

}
