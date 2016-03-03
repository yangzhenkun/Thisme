package com.example.yasin.thisme.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Yasin on 2016/3/2.
 *
 *
 * @{# ViewPagerAdapter.java Create on 2013-5-2 下午11:03:39
 *
 *     class desc: 引导页面适配器
 *
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 *
 *
 */
public class ViewPagerAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private AppCompatActivity activity;


    public ViewPagerAdapter(List<View> views, AppCompatActivity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
