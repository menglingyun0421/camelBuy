package com.lingyun.camelprocurementservice.orderfragment.aboutRemark;

import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.orderfragment.OrderDetail;
import com.lingyun.camelprocurementservice.orderfragment.aboutAddress.NewAddress;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class EditRemark extends BaseActivity {
    private ImageView order_title_back;
    private EditText newclient_et;
    private Button newclient_btn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remark);

        //判断是从哪个页面跳转过来的
        Intent intent=getIntent();
        final String page=intent.getStringExtra("key");

        newclient_et = findViewById(R.id.newclient_et);
        newclient_btn = findViewById(R.id.newclient_btn);
        order_title_back = findViewById(R.id.order_title_back);

        order_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRemark.this.finish();
            }
        });

        newclient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark=newclient_et.getText().toString();
                Message message=new Message();
                message.what=4;
                message.obj=remark;
                if (page.equals("detail")){
                    OrderDetail.handler.sendMessage(message);
                }else if (page.equals("newOrder")){
                    NewOrderActivity.handler.sendMessage(message);
                }
                EditRemark.this.finish();
            }
        });
    }
}
