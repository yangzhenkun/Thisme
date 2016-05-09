package com.example.yasin.thisme.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    TextView userNameTv;
    LinearLayout beiFenLL,tongBuLL,xiuGaiLL,ExitLL,banBenLL,yiJianLL;
    User user;
    AppCompatActivity mContext;
    ThismeDB thismeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_content);
        user = User.getInsstance();
        mContext = this;
        thismeDB = ThismeDB.getInsstance(this);
        initLayout();
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        beiFenLL = (LinearLayout) findViewById(R.id.user_beifen);
        xiuGaiLL = (LinearLayout) findViewById(R.id.user_edit_pass);
        tongBuLL = (LinearLayout) findViewById(R.id.user_download);
        tongBuLL.setOnClickListener(this);
        ExitLL = (LinearLayout) findViewById(R.id.user_tuichu);
        ExitLL.setOnClickListener(this);
        banBenLL = (LinearLayout) findViewById(R.id.user_refresh);
        yiJianLL = (LinearLayout) findViewById(R.id.user_advice);
        userNameTv = (TextView) findViewById(R.id.user_name);
        userNameTv.setText(user.getId());
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(R.color.light_blue);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final SharedPreferences pref = this.getSharedPreferences("count", Context.MODE_PRIVATE);
        int mycardCount = pref.getInt("mycard", 0);
        int friendcardCount = pref.getInt("friendcard",0);
        if(mycardCount!=0||friendcardCount!=0){
            final MaterialDialog materialDialog = new MaterialDialog(this);
            LinearLayout contentLL = (LinearLayout) this.getLayoutInflater().inflate(R.layout.null_layout,null);
            materialDialog.setCanceledOnTouchOutside(true)
                    .setContentView(contentLL)
                    .setTitle("有新添加的名片，请备份")
                    .setPositiveButton("备份", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("mycard",0);
                            editor.putInt("friendcard",0);
                            editor.commit();
                            materialDialog.dismiss();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                }
            }).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_beifen:

                break;
            case R.id.user_download:
                String url = Utils.baseUrl+"getallcard.html";
                RequestParams params = new RequestParams();
                params.put("username",user.getId());
                params.put("token",user.getToken());
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("rs",response.toString());
                        try {
                            if(response.getString("status").equals("0")){
                               String resContent = response.getString("allcard");
                               List<Card> listTemp = Utils.JsonArray2CardList(resContent);
                                for (Card mCard: listTemp) {
                                    thismeDB.saveCard(mCard);
                                    Log.e("card",mCard.toString());
                                }
                            }else{
                                Toast.makeText(mContext,"下载失败",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(mContext,"网络错误",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.user_edit_pass:
                break;
            case R.id.user_tuichu:
                user.setIsOnline(false);
                Toast.makeText(this,"已退出",Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.user_refresh:
                break;
            case R.id.user_advice:
                break;
        }
    }
}
