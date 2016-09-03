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

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.EditCardActivity;
import com.example.yasin.thisme.activity.QRCodeActivity;
import com.example.yasin.thisme.activity.ShowCardActivity;
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;
import com.example.yasin.thisme.model.User;
import com.example.yasin.thisme.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
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

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thismeDB = ThismeDB.getInsstance(this.getActivity().getApplicationContext());
        user = User.getInsstance();
        list = thismeDB.loadMyCard();
        mContent = (AppCompatActivity) this.getActivity();

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
                                String url = Utils.baseUrl+"deletecard.html";

                                AsyncHttpClient client = new AsyncHttpClient();
                                RequestParams params = new RequestParams();
                                params.put("username",user.getId());
                                params.put("token",user.getToken());
                                params.put("cardid",list.get(position).getCardIdFromS());
                                Log.e("yasin-s",params.toString());
                                client.post(url,params,new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Log.e("yasin",response.toString());
                                        try {
                                            if(response.getString("status").equals("0")){
                                                thismeDB.deleteCard(list.get(position).getCardId());
                                                list.remove(position);
                                                myAdapter.notifyItemRemoved(position);
                                                materialDialog.dismiss();
                                                Toast.makeText(mContent,"删除成功",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(mContent,"删除失败",Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                        Toast.makeText(mContent,"网络错误",Toast.LENGTH_SHORT).show();
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
