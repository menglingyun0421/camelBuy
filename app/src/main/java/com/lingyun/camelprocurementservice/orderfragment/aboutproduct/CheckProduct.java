package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lingyun.camelprocurementservice.R;

public class CheckProduct extends FragmentActivity implements View.OnClickListener{
    private TabLayout order_product_tabs;
    private ViewPager vp;
    private String title[] = {"全部","现货","母婴","个护美妆","营养保健","钟表","服装","轻奢","食品","家居","其它"};
    private List<Fragment> fragments;

    private ImageView order_title_back,order_title_rightIv;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_product);

        initView();
        initPager();
        //将ViewPager关联到TabLayout上
        order_product_tabs.setupWithViewPager(vp);
        setStatusBarColor(this, Color.RED);
    }

    private void initView(){
        vp =  findViewById(R.id.order_product_vp);
        order_product_tabs = findViewById(R.id.order_product_tabs);

        order_title_back=findViewById(R.id.order_title_back);
        order_title_rightIv=findViewById(R.id.order_title_rightIv);

        order_title_back.setOnClickListener(this);
        order_title_rightIv.setOnClickListener(this);
    }

    private void initPager() {
        // TODO Auto-generated method stub
        fragments = new ArrayList<Fragment>();
        Bundle bundle=new Bundle();
        bundle.putString("key","productchildfragment");
        ProductChildFragment_all all = new ProductChildFragment_all();
        all.setArguments(bundle);
        ProductChildFragment_stock stock = new ProductChildFragment_stock();
        stock.setArguments(bundle);
        ProductChildFragment_im im=new ProductChildFragment_im();
        im.setArguments(bundle);
        ProductChildFragment_makeup makeup=new ProductChildFragment_makeup();
        makeup.setArguments(bundle);
        ProductChildFragment_health health=new ProductChildFragment_health();
        health.setArguments(bundle);
        ProductChildFragment_watch watch=new ProductChildFragment_watch();
        watch.setArguments(bundle);
        ProductChildFragment_clothing clothing=new ProductChildFragment_clothing();
        clothing.setArguments(bundle);
        ProductChildFragment_luxury luxury=new ProductChildFragment_luxury();
        luxury.setArguments(bundle);
        ProductChildFragment_food food=new ProductChildFragment_food();
        food.setArguments(bundle);
        ProductChildFragment_home home=new ProductChildFragment_home();
        home.setArguments(bundle);
        ProductChildFragment_other other=new ProductChildFragment_other();
        other.setArguments(bundle);
        fragments.add(all);
        fragments.add(stock);
        fragments.add(im);
        fragments.add(makeup);
        fragments.add(health);
        fragments.add(watch);
        fragments.add(clothing);
        fragments.add(luxury);
        fragments.add(food);
        fragments.add(home);
        fragments.add(other);


        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_title_rightIv:
                Intent intent=new Intent(CheckProduct.this, NewProduct.class);
                intent.putExtra("key","checkproduct");
                startActivity(intent);
                CheckProduct.this.finish();
                break;
            case R.id.order_title_back:
                CheckProduct.this.finish();
                break;

        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return title[position];
        }

        @Override
        public long getItemId(int position) {
            if (fragments != null) {
                if (position < fragments.size()) {
                    return fragments.get(position).hashCode();       //important
                }
            }
            return super.getItemId(position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}
