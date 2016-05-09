package com.example.yasin.thisme.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.GuideActivity;
import com.example.yasin.thisme.activity.RegistActivity;
import com.example.yasin.thisme.activity.UserActivity;
import com.example.yasin.thisme.model.User;
import com.example.yasin.thisme.utils.Utils;
import com.example.yasin.thisme.utils.getMD5;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Yasin on 2016/1/29.
 */
public class MoreFragment extends Fragment implements View.OnClickListener{

    User user;
    RelativeLayout meLayout,talkLayout,aboutLayout,instructionLayout;
    AppCompatActivity mContent;
    TextView meTv;
    String phoneNum,pass;
    ProgressDialog progressDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123://login success
                    Toast.makeText(mContent,"登录成功",Toast.LENGTH_LONG).show();
                    break;
                case 0x321://login faile
                    Toast.makeText(mContent,"用户名或密码错误",Toast.LENGTH_LONG).show();
                    break;
                case 0x1234://network faile
                    Toast.makeText(mContent,"网络连接错误，请检查网络!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.more_fragment,container,false);
        mContent = (AppCompatActivity) this.getActivity();
        user = User.getInsstance();


        initLayout(root);

        if(user.isOnline()){
            meTv.setText(user.getId());
        }

        return root;
    }

    private void initLayout(LinearLayout root) {
        meLayout = (RelativeLayout) root.findViewById(R.id.more_me);
        meLayout.setOnClickListener(this);
        meTv = (TextView) root.findViewById(R.id.me_textView);
        talkLayout = (RelativeLayout) root.findViewById(R.id.more_jyjq);
        talkLayout.setOnClickListener(this);
        aboutLayout = (RelativeLayout) root.findViewById(R.id.more_set);
        aboutLayout.setOnClickListener(this);
        instructionLayout = (RelativeLayout) root.findViewById(R.id.more_instruction);
        instructionLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_me:
                if(user.isOnline()){
                    Intent intent = new Intent(mContent, UserActivity.class);
                    startActivity(intent);
                }else{
                    final SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    phoneNum = pref.getString("phoneNum","");
                    pass = pref.getString("pass","");
                    final MaterialDialog materialDialog = new MaterialDialog(mContent);
                    LinearLayout contentLL = (LinearLayout) mContent.getLayoutInflater().inflate(R.layout.activity_login,null);
                    final EditText phoneET,passET;
                    phoneET = ((EditText) contentLL.findViewById(R.id.et_phonenumber));
                    passET = ((EditText)contentLL.findViewById(R.id.et_password));
                    phoneET.setText(phoneNum);
                    passET.setText(pass);
                    final CheckBox checkBox = (CheckBox) contentLL.findViewById(R.id.save_zh_check);
                    materialDialog.setTitle("登录")
                            .setContentView(contentLL)
                            .setCanceledOnTouchOutside(true)
                            .setPositiveButton("登录", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showProgrssDialog();
                                    phoneNum = String.valueOf(phoneET.getText());
                                    pass = String.valueOf(passET.getText());
                                    if (checkBox.isChecked()) {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("phoneNum", phoneNum);
                                        editor.putString("pass", pass);
                                        editor.commit();
                                    }
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    String url = Utils.baseUrl+"login.html";
                                    RequestParams params = new RequestParams();
                                    params.put("username",phoneNum);
                                    String passMD5 = getMD5.MD5(pass);
                                    params.put("password",passMD5);
                                    Log.e("yasin", params.toString());
                                    client.post(url, params, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            try {
                                                Log.e("yasin","post1");
                                                Log.e("yasin",response.toString());
                                                if(response.getString("status").equals("0")){
                                                    Log.e("yasin","post2");
                                                    user.setIsOnline(true);
                                                    user.setId(phoneNum);
                                                    user.setToken(response.getString("token"));
                                                    Toast.makeText(mContent,"登录成功",Toast.LENGTH_LONG).show();
                                                    meTv.setText(phoneNum);
                                                }else{
                                                    Log.e("yasin","post3");
                                                    user.setIsOnline(false);
                                                    Toast.makeText(mContent,"登录失败",Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                Log.e("yasin","post4");
                                                user.setIsOnline(false);
                                                e.printStackTrace();
                                            }
                                            closeProgressDialog();
                                        }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            user.setIsOnline(false);
                                            closeProgressDialog();
                                            Toast.makeText(mContent,"网络错误",Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            user.setIsOnline(false);
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headers, responseString, throwable);
                                            user.setIsOnline(false);
                                        }
                                    });
                                    materialDialog.dismiss();
                                }
                            })
                            .setNegativeButton("注册", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent toR = new Intent(mContent, RegistActivity.class);
                                    startActivity(toR);
                                    materialDialog.dismiss();
                                }
                            });

                    materialDialog.show();

                }

            break;
            case R.id.more_jyjq:
                MaterialDialog materialDialog1 = new MaterialDialog(mContent);
                materialDialog1.setCanceledOnTouchOutside(true)
                        .setTitle("交友技巧")
                        .setMessage("还未上线，敬请期待!");
                materialDialog1.show();
                break;
            case R.id.more_set:
                MaterialDialog materialDialog = new MaterialDialog(mContent);
                materialDialog.setCanceledOnTouchOutside(true)
                        .setTitle("关于我们")
                        .setMessage("Power by 闷声发大财");
                materialDialog.show();
                break;
            case R.id.more_instruction:
                Intent intent = new Intent(mContent, GuideActivity.class);
                intent.putExtra("fromInstruction",true);
                startActivity(intent);
                break;
        }
    }

        /*
       * 显示进度对话框
       * */
        private void showProgrssDialog(){
            if(progressDialog == null){
                progressDialog = new ProgressDialog(mContent);
                progressDialog.setMessage("正在登陆");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        }
        /*
        * 关闭对话框
        * */
        private void closeProgressDialog(){
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}

