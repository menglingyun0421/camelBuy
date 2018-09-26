package com.lingyun.camelprocurementservice.clientfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.orderfragment.aboutAddress.NewAddress;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.NewClient;
import com.lingyun.camelprocurementservice.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDetail extends BaseActivity implements View.OnClickListener{
    private ImageView order_title_back,order_title_rightIv;
    private TextView order_title_midTv;
    private String name;
    private ListView ClientDetail_lv;
    private Button ClientDetail_btn_save,ClientDetail_btn_delete;
    private String address;//查sp的到的json数据
    private List<Map> addressList;//存放姓名和地址的关系
    private List<String> strList;//当前用户的查询出来的地址集合
    private List<String> resultList;//当前用户经过编辑之后的地址集合
    public static Handler handler;
    private ClientAddressAdapter addressAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");

        initView();
        initData();
        clientDetailHandler();
    }

    private void initView(){
        order_title_back=findViewById(R.id.order_title_back);
        order_title_midTv=findViewById(R.id.order_title_midTv);
        ClientDetail_lv=findViewById(R.id.ClientDetail_lv);
        ClientDetail_btn_save=findViewById(R.id.ClientDetail_btn_save);
        ClientDetail_btn_delete=findViewById(R.id.ClientDetail_btn_delete);
        order_title_rightIv=findViewById(R.id.order_title_rightIv);

        order_title_midTv.setText(name);
        ClientDetail_btn_save.setOnClickListener(this);
        order_title_back.setOnClickListener(this);
        order_title_rightIv.setOnClickListener(this);
    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
        address=sharedPreferences.getString(name, "camel");
        Log.e("mly_useraddress", "------------" + address);
        Gson gson = new Gson();
        if (address.equals("camel")) {
            return;
        }else {
            addressList = gson.fromJson(address, new TypeToken<List<Map>>() {
            }.getType());
            Log.e("mly_useraddress", "------------" + addressList);
        }

        strList=new ArrayList<>();
        for (int i=0;i<addressList.size();i++){
            strList.add((String) addressList.get(i).get("name"));
        }

        addressAdapter=new ClientAddressAdapter(ClientDetail.this,strList);
        ClientDetail_lv.setAdapter(addressAdapter);

        ClientDetail_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogUtils.showConfirmDialog(ClientDetail.this, "确定要删除该地址吗？", new View.OnClickListener() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ClientDetail_btn_save:
                save();
                break;

            case R.id.ClientDetail_btn_delete:
                break;

            case R.id.order_title_back:
                ClientDetail.this.finish();
                break;

            case R.id.order_title_rightIv:
                Intent intent=new Intent(ClientDetail.this, NewAddress.class);
                intent.putExtra("key","clientDetail");
                intent.putExtra("clientName",name);
                startActivity(intent);
                break;
        }
    }

    private void delete(int position){
        strList.remove(position);
        addressList.remove(position);
        Gson gson=new Gson();
        String deleteList=gson.toJson(addressList);
        SharedPreferences sharedPreferences=getSharedPreferences("clientAddress",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(name,deleteList);
        editor.commit();
        addressAdapter.notifyDataSetChanged();
        Toast.makeText(ClientDetail.this, "地址删除成功", Toast.LENGTH_LONG).show();
    }

    private void save(){
        if (resultList!=null){
            addressList.clear();
            for (int i=0;i<resultList.size();i++){
                Map map=new HashMap();
                map.put("clientName",name);
                map.put("name",resultList.get(i));
                addressList.add(map);
            }
            Gson gson=new Gson();
            String finalList=gson.toJson(addressList);
            SharedPreferences sharedPreferences = getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(name,finalList);
            editor.commit();
            Toast.makeText(ClientDetail.this, "地址编辑成功", Toast.LENGTH_LONG).show();
            ClientDetail.this.finish();
        }
    }

    public void clientDetailHandler(){
        handler=new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what){
                    case 1:
                        Log.e("clientDetail_mly","----------------------"+msg.obj);
                        resultList= (List<String>) msg.obj;
                        break;
                }
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
        initData();
        clientDetailHandler();
    }
}
