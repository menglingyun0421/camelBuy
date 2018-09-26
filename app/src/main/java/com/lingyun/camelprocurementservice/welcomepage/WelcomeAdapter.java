package com.lingyun.camelprocurementservice.welcomepage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 凌云 on 2018/7/24.
 * 欢迎页轮播图适配器
 */

public class WelcomeAdapter extends PagerAdapter{
    private List<ImageView> list;

    public WelcomeAdapter(List<ImageView>list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(list.get(position));
    }
}