package com.example.yasin.thisme.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yasin.thisme.R;
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;
import com.example.yasin.thisme.model.User;
import com.example.yasin.thisme.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Yasin on 2016/2/17.
 */
public class EditCardActivity extends AppCompatActivity implements View.OnClickListener{

        private Toolbar toolbar;
        private Button addBtn,cancelBtn,saveBtn;
        private LinearLayout[] contentLinearLayout = new LinearLayout[50];
        private EditText[] met = new EditText[50];
        private EditText[] met2 = new EditText[50];
        private ThismeDB thismeDB;
        private EditText etName,etPhoneNum,etEamil,etQQ,etWeixin,etMiaosu;
        private Map<String,String> more = new HashMap<String,String>();
        private int fillFlag=0;
        Card mCard;
        boolean fromScan;
        private AppCompatActivity mContext;
        private User user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_card);
            mContext = this;
            mCard = getIntent().getParcelableExtra("card");
            user = User.getInsstance();
            fromScan = getIntent().getBooleanExtra("from",false);
            initLayout();
        }

        private void initLayout() {
            final Intent intent = new Intent(this,MainActivity.class);
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_create_card_toolbar);
            toolbar.setTitle("修改自己的名片");
            toolbar.setTitleTextColor(R.color.light_blue);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_36dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                    finish();
                }
            });
            addBtn = (Button) findViewById(R.id.create_card_more_btn);
            addBtn.setOnClickListener(this);
            cancelBtn = (Button) findViewById(R.id.create_card_cancel_btn);
            cancelBtn.setOnClickListener(this);
            saveBtn = (Button) findViewById(R.id.create_card_save_btn);
            saveBtn.setOnClickListener(this);
            etName = (EditText) findViewById(R.id.create_card_name);
            etName.setText(mCard.getName());
            etPhoneNum = (EditText) findViewById(R.id.create_card_phonenum);
            etPhoneNum.setText(mCard.getPhoneNum());
            etEamil = (EditText) findViewById(R.id.create_card_email);
            etEamil.setText(mCard.getEmail());
            etQQ = (EditText) findViewById(R.id.create_card_qq);
            etQQ.setText(mCard.getQQ());
            etWeixin = (EditText) findViewById(R.id.create_card_weixin);
            etWeixin.setText(mCard.getWeixin());
            etMiaosu = (EditText) findViewById(R.id.create_card_miaosu);
            etMiaosu.setText(mCard.getMiaosu());
            String[] dataS = Utils.HashMapString2StringArray(mCard.getMore());
            for(int i=dataS.length-1;i>0;i-=2){
                LinearLayout container = (LinearLayout) findViewById(R.id.create_card_more_information);
                contentLinearLayout[fillFlag] = (LinearLayout) getLayoutInflater().inflate(R.layout.more_item, container, false);
                met[fillFlag] = (EditText) contentLinearLayout[fillFlag].findViewById(R.id.mycard_more_name);
                met2[fillFlag] = (EditText) contentLinearLayout[fillFlag].findViewById(R.id.mycard_more_content);
                met[fillFlag].setText(dataS[i-1]);
                met2[fillFlag].setText(dataS[i]);
                container.addView(contentLinearLayout[fillFlag]);
                fillFlag++;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.create_card_more_btn:
                    if(fillFlag<50){
                        addInformation();
                    }else{
                        Toast.makeText(this, "最多添加50条", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.create_card_cancel_btn:
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.create_card_save_btn:
                    mCard.setName(etName.getText().toString());
                    mCard.setPhoneNum(etPhoneNum.getText().toString());
                    mCard.setEmail(etEamil.getText().toString());
                    mCard.setQQ(etQQ.getText().toString());
                    mCard.setWeixin(etWeixin.getText().toString());
                    mCard.setMiaosu(etMiaosu.getText().toString());
                    for(int i=0;i<fillFlag;i++){
                        more.put(met[i].getText().toString(),met2[i].getText().toString());
                    }
                    mCard.setMore(more.toString());
                    thismeDB = ThismeDB.getInsstance(this);

                    if(fromScan){
                        mCard.setShuxing("2");
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("username",user.getId());
                        params.put("token", user.getToken());
                        params.put("cardinf", Utils.Card2JsonString(mCard));
                        String url = Utils.baseUrl+"addcard.html";
                        client.post(url,params,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                Log.e("yasin1",response.toString());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                Log.e("yasin2",responseString);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    Log.e("yasin",response.toString());
                                    if(response.getString("status").equals("0")){
                                        //得到cardID
                                        mCard.setCardIdFromS(response.getString("cardid"));
                                        //Log.e("card",card.toString());
                                        thismeDB.saveCard(mCard);
                                        Toast.makeText(mContext,"名片已添加",Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(mContext,MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }else{
                                        Log.e("yasin","failue");
                                        Toast.makeText(mContext,"保存失败",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Toast.makeText(mContext,"网络错误",Toast.LENGTH_SHORT).show();
                                Log.e("yasin","failue");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.e("yasin",errorResponse.toString());
                                Log.e("status",statusCode+"");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.e("yasin",responseString);
                                Log.e("status",statusCode+"");
                            }
                        });
  //                      thismeDB.saveCard(mCard);
//                        SharedPreferences mSharedPF = getSharedPreferences("count", Activity.MODE_PRIVATE);
//                        int count = mSharedPF.getInt("friendcard",0);
//                        count++;
//                        SharedPreferences.Editor editor = mSharedPF.edit();
//                        editor.putInt("friendcard", count);
//                        editor.commit();
                        Toast.makeText(mContext,"名片已保存，可到名片出查看",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(mContext,MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        AsyncHttpClient client = new AsyncHttpClient();
                        String url = Utils.baseUrl+"updatecard.html";
                        RequestParams params = new RequestParams();
                        params.put("username",user.getId());
                        params.put("token",user.getToken());
                        params.put("cardid",mCard.getCardIdFromS());
                        params.put("cardinf", Utils.Card2JsonString(mCard));
                        Log.e("painf",params.toString());
                        client.post(url, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (response.getString("status").equals("0")) {
                                        thismeDB.xiugaiCard(mCard);
                                        //Toast.makeText(mContext,"名片已保存，可到名片出查看",Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(mContext,MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mContext,"修改失败",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.e("err",""+statusCode);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.e("err",""+statusCode);
                            }
                        });

                    }
                    break;
            }
        }
        //动态添加内容
        private void addInformation() {
            LinearLayout container = (LinearLayout) findViewById(R.id.create_card_more_information);
            contentLinearLayout[fillFlag] = (LinearLayout) getLayoutInflater().inflate(R.layout.more_item, container, false);
            met[fillFlag] = (EditText) contentLinearLayout[fillFlag].findViewById(R.id.mycard_more_name);
            met2[fillFlag] = (EditText) contentLinearLayout[fillFlag].findViewById(R.id.mycard_more_content);
            container.addView(contentLinearLayout[fillFlag]);
            fillFlag++;
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
