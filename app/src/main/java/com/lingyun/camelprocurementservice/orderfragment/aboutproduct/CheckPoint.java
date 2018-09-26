package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

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
import android.widget.Button;
import android.widget.EditText;
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

import java.security.Key;
import java.util.List;

public class CheckPoint extends BaseActivity implements View.OnClickListener{
    private ImageView order_title_back,order_title_rightIv;
    private ListView checkclient_lv;
    private List<String> name;
    private String key;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point);

        Intent intent=getIntent();
        key =intent.getStringExtra("key");

        ininView();
        initData();

        checkclient_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String midname=name.get(position);
                Message message=new Message();
                message.what=1;
                message.obj=midname;
                if ("newproduct".equals(key)){
                    NewProduct.handler.sendMessage(message);
                }else if ("productdetail".equals(key)){
                    ProductDetail.handler.sendMessage(message);
                }else if ("newOrder".equals(key)){
                    message.what=5;
                    NewOrderActivity.handler.sendMessage(message);
                }else if ("detail".equals(key)){
                    message.what=6;
                    OrderDetail.handler.sendMessage(message);
                }
                CheckPoint.this.finish();
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
                CheckPoint.this.finish();
                break;
            case R.id.order_title_rightIv:
                Intent intent=new Intent(CheckPoint.this, NewPoint.class);
                intent.putExtra("key", key);
                startActivity(intent);
                CheckPoint.this.finish();
                break;
        }
    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("GoodPoint", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("point", "camel");
        Gson gson = new Gson();
        if (username.equals("camel")) {
            Log.e("mly_username", "------------" + "camel");
            return;
        }else {
            name = gson.fromJson(username, new TypeToken<List<String>>() {
            }.getType());
            Log.e("mly_username", "------------" + name);
            ClientNameAdapter adapter=new ClientNameAdapter(CheckPoint.this,name);
            checkclient_lv.setAdapter(adapter);
        }

    }
}
