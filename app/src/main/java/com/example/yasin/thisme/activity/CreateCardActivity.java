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
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;

import java.util.HashMap;
import java.util.Map;

public class CreateCardActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Button addBtn,cancelBtn,saveBtn;
    private LinearLayout[] contentLinearLayout = new LinearLayout[30];
    private int LinearLayoutCount=0;
    private ThismeDB thismeDB;
    private EditText etName,etPhoneNum,etEamil,etQQ,etWeixin,etMiaosu;
    private Map<String,String> more = new HashMap<String,String>();
    private int fillFlag=0;

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
        etName = (EditText) findViewById(R.id.create_card_name);
        etPhoneNum = (EditText) findViewById(R.id.create_card_phonenum);
        etEamil = (EditText) findViewById(R.id.create_card_email);
        etQQ = (EditText) findViewById(R.id.create_card_qq);
        etWeixin = (EditText) findViewById(R.id.create_card_weixin);
        etMiaosu = (EditText) findViewById(R.id.create_card_miaosu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_card_more_btn:
                if(fillFlag==1){
                    fillFlag=0;
                    addInformation();
                }else{
                    Toast.makeText(this,"请先填写完添加的内容在点击添加更多信息按钮",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.create_card_cancel_btn:
                finish();
                break;
            case R.id.create_card_save_btn:
                Card card = new Card();
                card.setShuxing("1");
                card.setName(etName.getText().toString());
                card.setPhoneNum(etPhoneNum.getText().toString());
                card.setEmail(etEamil.getText().toString());
                card.setQQ(etQQ.getText().toString());
                card.setWeixin(etWeixin.getText().toString());
                card.setMiaosu(etMiaosu.getText().toString());
                card.setMore(more.toString());
                thismeDB = ThismeDB.getInsstance(this);
                thismeDB.saveCard(card);
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
            met.setId((LinearLayoutCount + 100));
            met.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            EditText met2 = new EditText(this);
            met2.setHint("信息内容");
            met2.setId((LinearLayoutCount + 200));
            met2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentLinearLayout[LinearLayoutCount].addView(met);
            contentLinearLayout[LinearLayoutCount].addView(met2);
            container.addView(contentLinearLayout[LinearLayoutCount]);
            if(met.getText().toString()==null||met2.getText().toString()==null){
                //如果没有没有填写内容
            }else{
                fillFlag=1;
                more.put(met.getText().toString(),met2.getText().toString());
            }
            LinearLayoutCount++;
        }
    }
}
