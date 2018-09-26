package com.lingyun.camelprocurementservice.clientfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.ClientNameAdapter;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.NewClient;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.ActivityManagerUtils;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.List;

/**
 * Created by 凌云 on 2018/7/24.
 */

public class ClientFragment extends BaseFragment implements View.OnClickListener{
    private ImageView order_title_rightIv;
    private ListView checkclient_lv;
    private List<String> name;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client, container, false);

        checkclient_lv=view.findViewById(R.id.checkclient_lv);

        checkclient_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ClientDetail.class);
                intent.putExtra("name",name.get(position));
                startActivity(intent);
            }
        });

        checkclient_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogUtils.showConfirmDialog(getActivity(), "确定要删除该用户吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(position);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                },"确定","取消");
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void initView(){
        order_title_rightIv=getActivity().findViewById(R.id.order_title_rightIv);
//        checkclient_lv=getActivity().findViewById(R.id.checkclient_lv);

        order_title_rightIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_title_rightIv:
                Intent intent=new Intent(getActivity(),fragmentNewClient.class);
                startActivityForResult(intent,100);
                break;
        }
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("clientName", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "camel");
        Gson gson = new Gson();
        if (username.equals("camel")) {
            return;
        }else {
            name = gson.fromJson(username, new TypeToken<List<String>>() {
            }.getType());
            Log.e("mly_username", "------------" + name);
            ClientNameAdapter adapter=new ClientNameAdapter(getActivity(),name);
            checkclient_lv.setAdapter(adapter);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
    }

    private void delete(int id){
        name.remove(id);
        Gson gson=new Gson();
        String nameresult=gson.toJson(name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("clientName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",nameresult);
        editor.commit();
        initData();
    }
}
