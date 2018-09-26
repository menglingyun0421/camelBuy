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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.clientfragment.ClientDetail;
import com.lingyun.camelprocurementservice.orderfragment.OrderDetail;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.NewClient;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.ActivityManagerUtils;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAddress extends BaseActivity {
    private ImageView order_title_back;
    private EditText newclient_et;
    private Button newclient_btn;
    private List<Map> list;
    private String page,clientName;
    private Map map;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        //判断是从哪个页面跳转过来的
        Intent intent=getIntent();
        page=intent.getStringExtra("key");
        clientName=intent.getStringExtra("clientName");

        list = new ArrayList<>();
        newclient_et = findViewById(R.id.newclient_et);
        newclient_btn = findViewById(R.id.newclient_btn);
        order_title_back = findViewById(R.id.order_title_back);

        order_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAddress.this.finish();
            }
        });

        //保存按钮
        newclient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clientName == null || clientName.equals("")) {
                    DialogUtils.TipDialog(NewAddress.this, "请在新建订单页选择用户后填写地址！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    },"确定");
                } else {
                    String name = newclient_et.getText().toString();
                    map = new HashMap();
                    if (name.equals("")) {
                        Toast.makeText(NewAddress.this, "用户地址不能为空", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
                        String username = sharedPreferences.getString(clientName, "camel");
                        Gson gson = new Gson();
                        if (username.equals("camel")) {
                            list.clear();
                            map.put("name", name);
                            map.put("clientName", clientName);
                            list.add(map);
                            String nameStr = gson.toJson(list);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(clientName, nameStr);
                            editor.commit();
                            Log.e("mly_useraddress", "------------" + nameStr);
                            Toast.makeText(NewAddress.this, "保存地址成功", Toast.LENGTH_LONG).show();
                            newclient_et.setText("");
//                        ActivityManagerUtils.getInstance().finishActivityclass(CheckAddress.class);//先关闭之前的页面，避免重复创建
                            Message message = new Message();
                            message.what = 3;
                            message.obj = name;
                            if (page.equals("detail")) {
                                OrderDetail.handler.sendMessage(message);
                            } else if (page.equals("newOrder")) {
                                NewOrderActivity.handler.sendMessage(message);
                            }
//                        else if (page.equals("clientDetail")){
//                            ClientDetail.handler.sendMessage(message);
//                        }
                            NewAddress.this.finish();
                        } else {
                            List<Map> midList = gson.fromJson(username, new TypeToken<List<Map>>() {
                            }.getType());
                            list.clear();
                            list.addAll(midList);
                            map.put("name", name);
                            map.put("clientName", clientName);
                            list.add(map);
                            String nameStr = gson.toJson(list);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(clientName, nameStr);
                            editor.commit();
                            Log.e("mly_useraddress", "------------" + nameStr);
                            Toast.makeText(NewAddress.this, "保存用户成功", Toast.LENGTH_LONG).show();
                            newclient_et.setText("");
                            ActivityManagerUtils.getInstance().finishActivityclass(CheckClient.class);//先关闭之前的页面，避免重复创建
                            Message message = new Message();
                            message.what = 3;
                            message.obj = name;
                            if (page.equals("detail")) {
                                OrderDetail.handler.sendMessage(message);
                            } else if (page.equals("newOrder")) {
                                NewOrderActivity.handler.sendMessage(message);
                            }
                            NewAddress.this.finish();
                        }
                    }
                }
            }
        });

    }
}
