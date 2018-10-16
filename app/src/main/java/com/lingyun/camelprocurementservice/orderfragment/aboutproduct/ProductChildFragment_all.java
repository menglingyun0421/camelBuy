package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.basepage.LazyLoadFragment;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/8/7.
 */

public class ProductChildFragment_all extends LazyLoadFragment {
    private ListView checkProductlv;
    private List<Map> productList=new ArrayList<>();
    private String midList;
    private NewProductAdapter adapter;
    private EditText order_product_et;
    private List<Map> searList=new ArrayList<>();
    private List<Map> searchMidList=new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_child_product_all_search;
    }

    @Override
    protected void lazyLoad() {
        Bundle bundle=getArguments();
        final String key= (String) bundle.get("key");

        checkProductlv=findViewById(R.id.checkProductlv);
        order_product_et=findViewById(R.id.order_product_et);//新增了一个搜索功能
        order_product_et.addTextChangedListener(new TextWatcher() {//搜索功能监听器
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                productList.clear();
                searList.clear();
                searList.addAll(searchMidList);
                String serachNnam=order_product_et.getText().toString();
                if (!serachNnam.equals("")){
                    for (int i=0;i<searList.size();i++){
//                        if (searList.get(i).get("proudctName").equals(serachNnam)){
                        if (searList.get(i).get("proudctName").toString().contains(serachNnam)){
                            productList.add(searList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    productList.addAll(searchMidList);
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        midList = sharedPreferences.getString("productList", "camel");
        if (midList.equals("camel")){
            return;
        }else {
            Gson gson = new Gson();
            productList = gson.fromJson(midList, new TypeToken<List<Map>>() {}.getType());
            searchMidList.addAll(productList);
            adapter=new NewProductAdapter(getActivity(),productList);
            checkProductlv.setAdapter(adapter);

            checkProductlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("productfragment".equals(key)){
                        Gson gson = new Gson();
                        Map map=productList.get(position);
                        String productDetail=gson.toJson(map);
                        Intent intent=new Intent(getActivity(),ProductDetail.class);
                        intent.putExtra("key",productDetail);
                        startActivity(intent);

                    }else if ("productchildfragment".equals(key)){

                        Gson gson = new Gson();
                        Map map=productList.get(position);
                        Message message=new Message();
                        message.what=2;
                        message.obj=gson.toJson(map);
                        NewOrderActivity.handler.sendMessage(message);
                        getActivity().finish();
                    }
                }
            });

            checkProductlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Map map=productList.get(position);
                    final String productId= (String) map.get("productId");
                    final String productNameD= (String) map.get("proudctName");
                    DialogUtils.showConfirmDialog(getActivity(), "确认要删除该商品吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(productId, productNameD);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    },"确定","取消");

                    return true;
                }
            });
        }

    }

    @Override
    protected void stopLoad() {

    }

    private void delete(String productId,String productNameD){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        String midStr = sharedPreferences.getString("productList", "camel");
        Gson gson = new Gson();
        List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
        }.getType());

        for (int i = 0; i < midList.size(); i++) {
            if (productId!=null&&productId!=""&&midList.get(i).get("proudctId")!=null){
                if (productId.equals(midList.get(i).get("proudctId"))){
                    midList.remove(i);
                    productList.remove(i);
                }
            }else if (productNameD.equals(midList.get(i).get("proudctName"))) {
                midList.remove(i);
                productList.remove(i);
            }
        }

        String resultStr=gson.toJson(midList);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("productList",resultStr);
        editor.commit();

        adapter.notifyDataSetChanged();
    }
}
