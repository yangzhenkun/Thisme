package com.example.yasin.thisme.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.GuideActivity;
import com.example.yasin.thisme.activity.RegistActivity;
import com.example.yasin.thisme.activity.UserActivity;
import com.example.yasin.thisme.model.User;



import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Yasin on 2016/1/29.
 */
public class MoreFragment extends Fragment implements View.OnClickListener{

    User user;
    RelativeLayout meLayout,talkLayout,aboutLayout,instructionLayout;
    AppCompatActivity mContent;
    RequestQueue mRequestQueue;
    TextView meTv;
    String phoneNum,pass;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.more_fragment,container,false);
        mContent = (AppCompatActivity) this.getActivity();
        mRequestQueue = Volley.newRequestQueue(mContent);
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
                                    user.setIsOnline(true);
                                    user.setId("Yasin");
                                    if(checkBox.isChecked()){
                                        phoneNum = String.valueOf(phoneET.getText());
                                        pass = String.valueOf(phoneET.getText());
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("phoneNum",phoneNum);
                                        editor.putString("pass",pass);
                                        editor.commit();
                                    }
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://169.254.107.217:8080/day10/CServlet", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            user.setIsOnline(true);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });
                                    mRequestQueue.add(stringRequest);
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
}
