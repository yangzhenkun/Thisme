package com.example.yasin.thisme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.model.Card;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yasin on 2016/1/29.
 */
public class CardFragment extends Fragment{
    Card myCard ;
    Map<String,String> cardMore = new HashMap<String,String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.card_fragment,container,false);
    }
}
