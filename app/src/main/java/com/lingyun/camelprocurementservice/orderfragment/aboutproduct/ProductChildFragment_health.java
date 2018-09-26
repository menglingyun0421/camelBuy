package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.LazyLoadFragment;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/8/7.
 */

public class ProductChildFragment_health extends LazyLoadFragment {
    private ListView checkProductlv;
    private List<Map> productList;
    private String midList;
    private List<Map> imList;
    private String key;
    private NewProductAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_child_product_all;
    }

    @Override
    protected void lazyLoad() {
        key=getKey();
        checkProductlv=findViewById(R.id.checkProductlv);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        midList = sharedPreferences.getString("productList", "camel");
        if (midList.equals("camel")){
            return;
        }else {
            Gson gson = new Gson();
            productList = gson.fromJson(midList, new TypeToken<List<Map>>() {
            }.getType());
            imList = new ArrayList<>();
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).get("proudctClassify").equals("营养保健")) {
                    imList.add(productList.get(i));
                }
            }
            if (imList != null) {
                adapter = new NewProductAdapter(getActivity(), imList);
                checkProductlv.setAdapter(adapter);
            }

            checkProductlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("productfragment".equals(key)) {
                        Gson gson = new Gson();
                        Map map = imList.get(position);
                        String productDetail = gson.toJson(map);
                        Intent intent = new Intent(getActivity(), ProductDetail.class);
                        intent.putExtra("key", productDetail);
                        startActivity(intent);

                    } else if ("productchildfragment".equals(key)) {

                        Gson gson = new Gson();
                        Map map = imList.get(position);
                        Message message = new Message();
                        message.what = 2;
                        message.obj = gson.toJson(map);
                        NewOrderActivity.handler.sendMessage(message);
                        getActivity().finish();
                    }
                }
            });

            checkProductlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Map map = imList.get(position);
                    final String productId = (String) map.get("productId");
                    final String productNameD = (String) map.get("proudctName");
                    DialogUtils.showConfirmDialog(getActivity(), "确认要删除该商品吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(productId, productNameD, position);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }, "确定", "取消");

                    return true;
                }
            });
        }
    }

    private void delete(String productId,String productNameD,int position){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        String midStr = sharedPreferences.getString("productList", "camel");
        Gson gson = new Gson();
        List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
        }.getType());

        for (int i = 0; i < midList.size(); i++) {
            if (productId!=null&&productId!=""&&midList.get(i).get("proudctId")!=null){
                if (productId.equals(midList.get(i).get("proudctId"))){
                    midList.remove(i);
                    imList.remove(position);
                }
            }else if (productNameD.equals(midList.get(i).get("proudctName"))) {
                midList.remove(i);
                imList.remove(position);
            }
        }

        String resultStr=gson.toJson(midList);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("productList",resultStr);
        editor.commit();

        adapter.notifyDataSetChanged();
    }
}
