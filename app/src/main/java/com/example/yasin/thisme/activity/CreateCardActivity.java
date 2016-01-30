package com.example.yasin.thisme.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yasin.thisme.R;

public class CreateCardActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Button addBtn,cancelBtn,saveBtn;
    private LinearLayout[] contentLinearLayout = new LinearLayout[30];
    private int LinearLayoutCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        initLayout();
    }

    private void initLayout() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_create_card_toolbar);
        toolbar.setTitle("创建自己的名片");
        toolbar.setTitleTextColor(R.color.light_blue);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn = (Button) findViewById(R.id.create_card_more_btn);
        addBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.create_card_cancel_btn);
        cancelBtn.setOnClickListener(this);
        saveBtn = (Button) findViewById(R.id.create_card_save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_card_more_btn:
                addInformation();
                break;
            case R.id.create_card_cancel_btn:
                finish();
                break;
            case R.id.create_card_save_btn:
                Toast.makeText(this,"名片已保存，可到名片出查看",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
    //动态添加内容
    private void addInformation() {
        if(LinearLayoutCount==30){
            Toast.makeText(this,"已受限不能再添加",Toast.LENGTH_SHORT).show();
        }else{
            LinearLayout container = (LinearLayout) findViewById(R.id.create_card_more_information);
            contentLinearLayout[LinearLayoutCount] = new LinearLayout(this);
            contentLinearLayout[LinearLayoutCount].setId(LinearLayoutCount);
            contentLinearLayout[LinearLayoutCount].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentLinearLayout[LinearLayoutCount].setOrientation(LinearLayout.HORIZONTAL);
            EditText met = new EditText(this);
            met.setHint("信息名");
            met.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            EditText met2 = new EditText(this);
            met2.setHint("信息内容");
            met2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentLinearLayout[LinearLayoutCount].addView(met);
            contentLinearLayout[LinearLayoutCount].addView(met2);
            container.addView(contentLinearLayout[LinearLayoutCount]);
            LinearLayoutCount++;
        }
    }
}
