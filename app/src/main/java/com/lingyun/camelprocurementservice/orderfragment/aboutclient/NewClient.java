package com.lingyun.camelprocurementservice.orderfragment.aboutclient;

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
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.ActivityManagerUtils;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class NewClient extends BaseActivity {
    private ImageView order_title_back;
    private EditText newclient_et;
    private Button newclient_btn;
    private List<String> list;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        list = new ArrayList<>();
        newclient_et = findViewById(R.id.newclient_et);
        newclient_btn = findViewById(R.id.newclient_btn);
        order_title_back = findViewById(R.id.order_title_back);

        order_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewClient.this.finish();
            }
        });

        //保存按钮
        newclient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newclient_et.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(NewClient.this, "用户昵称不能为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("clientName", Context.MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", "camel");
                    Gson gson = new Gson();
                    if (username.equals("camel")) {
                        list.clear();
                        list.add(name);
                        String nameStr = gson.toJson(list);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", nameStr);
                        editor.commit();
                        Log.e("mly_username", "------------" + nameStr);
                        Toast.makeText(NewClient.this, "保存用户成功", Toast.LENGTH_LONG).show();
                        newclient_et.setText("");
                        ActivityManagerUtils.getInstance().finishActivityclass(CheckClient.class);//先关闭之前的页面，避免重复创建
                        Message message=new Message();
                        message.what=1;
                        message.obj=name;
                        NewOrderActivity.handler.sendMessage(message);
                        NewClient.this.finish();
                    } else {
                        boolean islife=false;
                        List<String> midList = gson.fromJson(username, new TypeToken<List<String>>() {
                        }.getType());
                        list.clear();
                        list.addAll(midList);
                        for (int i=0;i<midList.size();i++){
                            if (name.equals(midList.get(i))){
                                DialogUtils.TipDialog(NewClient.this,"用户名已存在，请更换用户名", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                },"确定");
                                islife=true;
                            }
                        }
                        if (islife){
                            return;
                        }else {
                            list.add(name);
                            String nameStr = gson.toJson(list);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", nameStr);
                            editor.commit();
                            Log.e("mly_username", "------------" + nameStr);
                            Toast.makeText(NewClient.this, "保存用户成功", Toast.LENGTH_LONG).show();
                            newclient_et.setText("");
                            ActivityManagerUtils.getInstance().finishActivityclass(CheckClient.class);//先关闭之前的页面，避免重复创建
                            Message message=new Message();
                            message.what=1;
                            message.obj=name;
                            NewOrderActivity.handler.sendMessage(message);
                            NewClient.this.finish();
                        }
                    }
                }
            }
        });

    }
}
