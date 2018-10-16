package com.lingyun.camelprocurementservice.orderfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/31.
 */

public class OrderUnfinish extends BaseFragment{
    private ListView order_unfinish_lv;
    private List<Map> orderList;
    private TextView order_unfinish_tv;
    private LinearLayout order_unfinish_ll;
    private EditText order_unfinish_et;
    private List<Map> orderUnfinshList=new ArrayList<>();
    private List<Map> searList=new ArrayList<>();
    private OrderAllAdapter adapter;
    private List<Map> searchMidList=new ArrayList<>();
    private List<Map> timeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_order_unfinish, container, false);
        order_unfinish_lv=view.findViewById(R.id.order_unfinish_lv);
        order_unfinish_ll=view.findViewById(R.id.order_unfinish_ll);
        order_unfinish_tv=view.findViewById(R.id.order_unfinish_tv);
        order_unfinish_et=view.findViewById(R.id.order_unfinish_et);

        order_unfinish_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searList.clear();
                timeList.clear();
                timeList.addAll(searchMidList);
                String serachNnam=order_unfinish_et.getText().toString();
                if (!serachNnam.equals("")){
                    for (int i=0;i<timeList.size();i++){
                        if (timeList.get(i).get("clientName").toString().contains(serachNnam)){
                            searList.add(timeList.get(i));
                        }
                    }
                    timeList.clear();
                    timeList.addAll(searList);
                    adapter.notifyDataSetChanged();
                    order_unfinish_tv.setText(timeList.size()+"");
                }else {
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                        order_unfinish_tv.setText(timeList.size()+"");
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();

        order_unfinish_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson=new Gson();
                String order=gson.toJson(timeList.get(position));
                Intent intent=new Intent();
                intent.setClass(getActivity(),OrderDetail.class);
                intent.putExtra("order", order );
                startActivity(intent);
            }
        });
    }

    private void initView(){
//        order_unfinish_lv=getActivity().findViewById(R.id.order_unfinish_lv);
//        order_unfinish_tv=getActivity().findViewById(R.id.order_unfinish_tv);
//        order_unfinish_ll=getActivity().findViewById(R.id.order_unfinish_ll);
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String midList = sharedPreferences.getString("orderList", "camel");
        Gson gson = new Gson();
        if (midList.equals("camel")) {
            order_unfinish_ll.setVisibility(View.GONE);
            return;
        }else {
            orderList = gson.fromJson(midList, new TypeToken<List<Map>>() {
            }.getType());
            Log.e("mly_orderStr", "------------" + orderList);
            orderUnfinshList.clear();
            searchMidList.clear();
            for (int i=0;i<orderList.size();i++){
                Map map= (Map) orderList.get(i);
                if (((String) map.get("proudctIsReceived")).equals("false")||((String) map.get("proudctIsStock")).equals("false")
                        ||((String) map.get("proudctIsPayment")).equals("false")||((String) map.get("proudctIsDeliver")).equals("false")
                        ||("false").equals((String) map.get("proudctIsHarvest"))){
                    orderUnfinshList.add(orderList.get(i));
                }
            }

            order_unfinish_tv.setText(orderUnfinshList.size()+"");
            Log.e("mly_orderStr", "------------" + orderUnfinshList);
            timeList=new ArrayList<>();
            for (int i=orderUnfinshList.size();i>0;i--){
                timeList.add(orderUnfinshList.get(i-1));
            }
            searchMidList.addAll(timeList);
            adapter=new OrderAllAdapter(getActivity(),timeList);
            order_unfinish_lv.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
    }
}
