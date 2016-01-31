package com.example.yasin.thisme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.MainActivity;
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yasin on 2016/1/29.
 */
public class CardFragment extends Fragment{
    private ThismeDB thismeDB ;
    Card myCard ;
    Map<String,String> cardMore = new HashMap<String,String>();
    List<Card> list = new ArrayList<Card>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thismeDB = ThismeDB.getInsstance(this.getActivity().getApplicationContext());
        list = thismeDB.loadMyCard();
        for(Card mcard:list){
            Log.e("yzk",mcard.getName());
            Log.e("id",String.valueOf(mcard.getCardId()));
            thismeDB.deleteCard(mcard.getCardId());
        }
        return inflater.inflate(R.layout.card_fragment,container,false);
    }
}
