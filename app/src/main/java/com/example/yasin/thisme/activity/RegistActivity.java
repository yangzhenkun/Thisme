package com.example.yasin.thisme.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yasin.thisme.R;


public class RegistActivity extends Activity implements OnClickListener {


    private static final String TAG = "RegistActivity";
    private EditText etPhoneNumber;
    private EditText etPassword;
    private EditText etName;//姓名
    private Button btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        etPhoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        etPassword = (EditText) findViewById(R.id.et_password);
        etName = (EditText) findViewById(R.id.et_name);
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
                if(etName.getText() == null || etName.getText().length()>20) {
                    Toast.makeText(this, "姓名不为空或长度不超过20", Toast.LENGTH_SHORT).show();
                    return ;
                }
                break;
            default:
                break;
        }
    }
}

