package com.example.yasin.thisme.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.model.User;
import com.example.yasin.thisme.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
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

import cz.msebera.android.httpclient.Header;


public class RegistActivity extends Activity implements OnClickListener {


    private static final String TAG = "RegistActivity";
    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnRegist;
    private Activity mContext;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mContext = this;
        etPhoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegist = (Button) findViewById(R.id.btn_regist);

        btnRegist.setOnClickListener(this);//设置注册按钮的点击事件

    }

    //使用HttpClient方式向服务器发送get请求
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_regist:

                if (etPhoneNumber.getText() ==null  || (etPhoneNumber.getText().length() !=11)) {
                    Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(etPassword.getText() == null || etPassword.getText().length()<3 || etPassword.getText().length()>15) {
                    Toast.makeText(this, "密码长度不在3-15位", Toast.LENGTH_SHORT).show();
                    return ;
                }
//                if(etName.getText() == null || etName.getText().length()>20) {
//                    Toast.makeText(this, "姓名不为空或长度不超过20", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
                AsyncHttpClient clent = new AsyncHttpClient();
                final String url = Utils.baseUrl+"regist.html";
                final RequestParams params = new RequestParams();
                params.put("username",etPhoneNumber.getText());
                params.put("password",etPassword.getText());
                clent.post(url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            Log.e("rs",response.getString("status"));
                            if(response.getString("status").equals("0")){
                                user = User.getInsstance();
                                user.setIsOnline(true);
                                user.setToken(response.getString("token"));
                                user.setId(etPhoneNumber.getText().toString());
                                Toast.makeText(mContext,"注册成功",Toast.LENGTH_LONG).show();
                                mContext.finish();
                            }else{
                                Toast.makeText(mContext,"改用已注册",Toast.LENGTH_LONG).show();
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

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("yasin","statusCode="+statusCode);
                        Log.e("yasin","headers="+headers.toString());
                        Log.e("yasin", "responseString="+responseString);
                        Log.e("yasin","throwable"+throwable.toString());
                        Toast.makeText(mContext,"网络错误1",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            default:
                break;
        }
    }
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            Log.e("yasin",url);
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
            Log.e("result",result);
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

