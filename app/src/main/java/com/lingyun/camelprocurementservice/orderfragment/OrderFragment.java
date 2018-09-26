package com.lingyun.camelprocurementservice.orderfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;

/**
 * Created by 凌云 on 2018/7/24.
 */

public class OrderFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    private Button order_title_leftBtn;
    private TextView order_title_midTv;
    private ImageView order_title_rightIv;
    private FrameLayout activity_order_container;
    private RadioGroup rgBottom;
    private FragmentManager manager;//v4下的Fragment
    private Fragment[] fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取fragment管理器
        manager = getActivity().getSupportFragmentManager();
        initFragment();
        initView();
        initView();
        onClick();
    }

    /**
     * 初始化Fragment，放置到数组中
     */
    private void initFragment() {
        fragments = new Fragment[2];

        OrderUnfinish orderFragment = new OrderUnfinish();
        fragments[0] = orderFragment;

        OrderAll clientFragment = new OrderAll();
        fragments[1] = clientFragment;
    }

    private void initView(){
        order_title_leftBtn=getActivity().findViewById(R.id.order_title_leftBtn);
        order_title_midTv=getActivity().findViewById(R.id.order_title_midTv);
        order_title_rightIv=getActivity().findViewById(R.id.order_title_rightIv);

        rgBottom = getActivity().findViewById(R.id.home_rg_order);
        rgBottom.setOnCheckedChangeListener(this);
        //设置第一个默认选中
        ((RadioButton) rgBottom.getChildAt(0)).setChecked(true);
    }

    public void onClick(){
        order_title_rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), NewOrderActivity.class));
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //导航实现fragment切换
        FragmentTransaction transaction = manager.beginTransaction();
        switch (checkedId){
            case R.id.order_rb_unfinish:
                transaction.replace(R.id.activity_order_container,fragments[0]);
                break;
            case R.id.order_rb_finish:
                transaction.replace(R.id.activity_order_container,fragments[1]);
                break;
        }
        transaction.commit();
    }
}
