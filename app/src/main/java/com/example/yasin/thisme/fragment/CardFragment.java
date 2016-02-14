package com.example.yasin.thisme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.activity.MainActivity;
import com.example.yasin.thisme.model.Card;
import com.example.yasin.thisme.model.ThismeDB;

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
    RecyclerView mRecyclerView;
    Card myCard ;
    Map<String,String> cardMore = new HashMap<String,String>();
    List<Card> list = new ArrayList<Card>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thismeDB = ThismeDB.getInsstance(this.getActivity().getApplicationContext());
        list = thismeDB.loadMyCard();


        mRecyclerView = new RecyclerView(this.getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(new MyAdapter((AppCompatActivity) this.getActivity()));

        return mRecyclerView;
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        /*
        * 一般传递归来一个content
        * */
        private AppCompatActivity mContent;
        /*
        * 由于fragment中有inflate这个对象，所以可以直接获取，不用再content中在获取，这里这两种方法都行
        * */
        private LayoutInflater mInflater;

        class ThismeViewHolder extends RecyclerView.ViewHolder{

            private TextView tvName,tvMiaosu;
            private Button shareBtn,editBtn,deleteBtn;
            public ThismeViewHolder(final View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.mycard_name);
                tvMiaosu = (TextView) itemView.findViewById(R.id.mycard_miaosu);
                shareBtn = (Button) itemView.findViewById(R.id.mycard_share);
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享操作
                    }
                });
                editBtn = (Button) itemView.findViewById(R.id.mycard_edit);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //编辑操作
                    }
                });
                deleteBtn = (Button) itemView.findViewById(R.id.mycard_delete);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog materialDialog = new MaterialDialog(mContent)
                                .setCanceledOnTouchOutside(true)
                                .setTitle("是否删除")
                                .setPositiveButton("是", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .setNegativeButton("否", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        materialDialog.show();
                    }
                });
            }

            public TextView getTv(int flag){
                if(flag==1){
                    return tvName;
                }else{
                    return tvMiaosu;
                }
            }
        }

        public MyAdapter(AppCompatActivity mContent){
            this.mContent = mContent;
        }
        public MyAdapter(LayoutInflater mInflater){
            this.mInflater = mInflater;
        }

        /*
        * 设置item格式
        * */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ThismeViewHolder holder = new ThismeViewHolder(LayoutInflater.from(mContent)
                    .inflate(R.layout.mycard_card_view, parent,
                            false));
            return holder;
        }

        /*
        * 绑定数据
        * */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ThismeViewHolder vh = (ThismeViewHolder) holder;
            TextView tvName = vh.getTv(1);
            TextView tvMiaosu = vh.getTv(2);
            Card mCard;
            mCard = list.get(position);
            tvName.setText(mCard.getName());
            tvMiaosu.setText(mCard.getMiaosu());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
