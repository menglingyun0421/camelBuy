package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.ClientNameAdapter;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/26.
 */

public class ProductChildFragment extends BaseFragment {
    private List<Map> productList=new ArrayList<>();
    private List<Map> classiflyProductList =new ArrayList<>();
    private ListView checkProductlv;
    private TextView checkProductTv,checkProductTvT;
    private String title;
    private String midList;
    private Boolean choiceList=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_child_product, container,false);

        Bundle bundle=getArguments();
        title= (String) bundle.get("key");
        Log.e("mly_productTitle", "------------" + title);

        checkProductTvT=view.findViewById(R.id.checkProductTvT);
        checkProductTvT.setText(title);

        if (title.equals("全部")){
            choiceList=true;
        } else {
            choiceList=false;
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        midList = sharedPreferences.getString("productList", "camel");
        Gson gson = new Gson();
        productList = gson.fromJson(midList, new TypeToken<List<Map>>() {}.getType());

        if (choiceList){
            initData(productList);
        }else {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).get("proudctClassify").equals(title)) {
                    classiflyProductList.add(productList.get(i));
                }
            }
            initData(classiflyProductList);
        }

        checkProductlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                Map map=productList.get(position);
                Message message=new Message();
                message.what=2;
                message.obj=gson.toJson(map);
                NewOrderActivity.handler.sendMessage(message);
                getActivity().finish();
            }
        });
    }

    private void initView(){
        checkProductlv=getActivity().findViewById(R.id.checkProductlv);
        checkProductTv=getActivity().findViewById(R.id.checkProductTv);
    }

    private void initData(List<Map> list){
        if (midList.equals("camel")) {
            return;
        }else if (list.size()==0) {
            return;
        }else {
            Log.e("mly_productStr", "------------" + list);
            NewProductAdapter adapter=new NewProductAdapter(getActivity(),list);
            checkProductlv.setAdapter(adapter);
        }
    }
}
