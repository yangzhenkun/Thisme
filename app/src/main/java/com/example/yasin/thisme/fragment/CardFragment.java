package com.example.yasin.thisme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.EditCardActivity;
import com.example.yasin.thisme.activity.QRCodeActivity;
import com.example.yasin.thisme.activity.ShowCardActivity;
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;
import com.example.yasin.thisme.model.User;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Yasin on 2016/1/29.
 */
public class CardFragment extends Fragment{
    private ThismeDB thismeDB ;
    AppCompatActivity mContent;
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    List<Card> list = new ArrayList<Card>();
    User user;
    RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thismeDB = ThismeDB.getInsstance(this.getActivity().getApplicationContext());
        user = User.getInsstance();
        list = thismeDB.loadMyCard();
        mContent = (AppCompatActivity) this.getActivity();
        mRequestQueue = Volley.newRequestQueue(mContent);

        mRecyclerView = new RecyclerView(this.getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        myAdapter = new MyAdapter((AppCompatActivity) this.getActivity(),list);
        myAdapter.setOnItemClickLitener(new MyAdapter.MyAdapterClickLitener() {
            @Override
            public void OnItemClick(View view, int position) {
              /*
              * item监听,点击获取名片详细内容
              * */
                Card mCard = list.get(position);
                Intent intent = new Intent(mContent, ShowCardActivity.class);
                intent.putExtra("card", mCard);
                startActivity(intent);
            }

            @Override
            public void OnShareBtn(int position) {
                Card mCard = list.get(position);
                Intent intent = new Intent(mContent, QRCodeActivity.class);
                intent.putExtra("card", mCard);
                startActivity(intent);
            }

            @Override
            public void OnEditBtn(int position) {
                if (user.isOnline()) {
                    Card mCard = list.get(position);
                    Intent intent = new Intent(mContent, EditCardActivity.class);
                    intent.putExtra("card", mCard);
                    startActivity(intent);
                    mContent.finish();
                }else{
                    Toast.makeText(mContent,"请登录在操作",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void OnDeleteBtn(final int position) {
                final MaterialDialog materialDialog = new MaterialDialog(mContent);
                LinearLayout nullLayout = (LinearLayout) mContent.getLayoutInflater().inflate(R.layout.null_layout, null);
                materialDialog.setTitle("是否删除")
                        .setContentView(nullLayout)
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                thismeDB.deleteCard(list.get(position).getCardId());
                                list.remove(position);
                                myAdapter.notifyItemRemoved(position);
                                materialDialog.dismiss();
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://169.254.107.217:8080/day10/CServlet", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("否", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        })
                        .setCanceledOnTouchOutside(true);
                if (user.isOnline()) {
                    materialDialog.show();
                }else{
                    Toast.makeText(mContent,"请登录在操作",Toast.LENGTH_LONG).show();
                }

            }
        });
        mRecyclerView.setAdapter(myAdapter);
        return mRecyclerView;
    }

}
