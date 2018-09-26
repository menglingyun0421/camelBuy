package com.lingyun.camelprocurementservice.orderfragment.aboutAddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.orderfragment.OrderDetail;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.ClientNameAdapter;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.NewClient;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.ActivityManagerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckAddress extends BaseActivity implements View.OnClickListener{
    private ImageView order_title_back,order_title_rightIv;
    private ListView checkclient_lv;
    private List<Map> addressList;
    private String page,clientName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_address);

        //判断是从哪个页面跳转过来的
        Intent intent=getIntent();
        page=intent.getStringExtra("key");
        clientName=intent.getStringExtra("clientName");

        ininView();
        initData();

        checkclient_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map=addressList.get(position);
                String midname= (String) map.get("name");
                Message message=new Message();
                message.what=3;
                message.obj=midname;
                if (page.equals("detail")){
                    OrderDetail.handler.sendMessage(message);
                }else if (page.equals("newOrder")){
                    NewOrderActivity.handler.sendMessage(message);
                }
                CheckAddress.this.finish();
            }
        });
    }

    private void ininView(){
        order_title_back=findViewById(R.id.order_title_back);
        order_title_rightIv=findViewById(R.id.order_title_rightIv);
        checkclient_lv=findViewById(R.id.checkclient_lv);

        order_title_back.setOnClickListener(this);
        order_title_rightIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_title_back:
                CheckAddress.this.finish();
                break;
            case R.id.order_title_rightIv:
//                startActivity(new Intent().setClass(CheckAddress.this, NewAddress.class));
                Intent intent=new Intent();
                intent.putExtra("key",page);
                intent.putExtra("clientName",clientName);
                intent.setClass(CheckAddress.this, NewAddress.class);
                startActivity(intent);
                //将NewOrderActivity添加到Activity管理器中，在需要的时候关闭，避免重复创建
//                ActivityManagerUtils.getInstance().addActivity(CheckAddress.this);
                CheckAddress.this.finish();
                break;
        }
    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(clientName, "camel");
        Gson gson = new Gson();
        if (address.equals("camel")) {
            return;
        }else {
            addressList = gson.fromJson(address, new TypeToken<List<Map>>() {
            }.getType());
            Log.e("mly_useraddress", "------------" + addressList);
            List<String> list=new ArrayList<>();
            for (int i=0;i<addressList.size();i++){
                String midStr= (String)addressList.get(i).get("name");
                list.add(midStr);
            }
            ClientNameAdapter adapter=new ClientNameAdapter(CheckAddress.this,list);
            checkclient_lv.setAdapter(adapter);
        }

    }
}
