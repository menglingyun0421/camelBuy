package com.lingyun.camelprocurementservice.productfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckPoint;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckProduct;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.NewProduct;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_all;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_clothing;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_food;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_health;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_home;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_im;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_luxury;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_makeup;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_other;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_stock;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductChildFragment_watch;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.ProductDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凌云 on 2018/7/24.
 */

public class ProductFragment extends BaseFragment implements View.OnClickListener{
    private TabLayout order_product_tabs;
    private ViewPager vp;
    private String title[] = {"全部","现货","母婴","个护美妆","营养保健","钟表","服装","轻奢","食品","家居","其它"};
    private List<Fragment> fragments;

    private ImageView order_title_rightIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initPager();
        //将ViewPager关联到TabLayout上
        order_product_tabs.setupWithViewPager(vp);
    }

    private void initView(){
        vp =  getActivity().findViewById(R.id.order_product_vp);
        order_product_tabs = getActivity().findViewById(R.id.order_product_tabs);

        order_title_rightIv=getActivity().findViewById(R.id.order_title_rightIv);

        order_title_rightIv.setOnClickListener(this);
    }

    private void initPager() {
        // TODO Auto-generated method stub
        fragments = new ArrayList<Fragment>();
        Bundle bundle=new Bundle();
        bundle.putString("key","productfragment");
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


        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
        vp.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_title_rightIv:
                Intent intent=new Intent(getActivity(), NewProduct.class);
                intent.putExtra("key","productfragment");
                startActivity(intent);
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
}
