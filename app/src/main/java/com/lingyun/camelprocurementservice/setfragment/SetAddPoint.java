package com.lingyun.camelprocurementservice.setfragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.point.GetPoint;
import com.lingyun.camelprocurementservice.utils.Aes;
import com.lingyun.camelprocurementservice.utils.DialogUtils;
import com.lingyun.camelprocurementservice.utils.FileUtils;

public class SetAddPoint extends BaseActivity {
    private Button newclient_btn;
    private EditText  newclient_et;
    private ImageView order_title_back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_add_point);

        initVeiw();
        boolean isMk=FileUtils.makeRootDirectory("/sdcard/Android/data/com.systemcamel.point");
        if (!isMk){
            StringBuffer buffer=new StringBuffer();
            buffer.append("6063CD7E3295A1988107DD03B9A4974E"+"##");
            buffer.append("928B7AF1D21E507D055193AB82B6EBBA"+"##");
            buffer.append("5465F04950BAF2ACE1000DA0E52B2106");
            FileUtils.saveData(buffer.toString(),"/Android/data/com.systemcamel.point","camelPoint.txt");
        }
//        initData();
    }

    private void initVeiw(){
        newclient_btn=findViewById(R.id.newclient_btn);
        newclient_et=findViewById(R.id.newclient_et);
        order_title_back=findViewById(R.id.order_title_back);

        order_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAddPoint.this.finish();
            }
        });

        newclient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String point = newclient_et.getText().toString();
                if (GetPoint.isLive(point)){
                    String moneyPiont=FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
                    String masterPassword = "tebiefuza";
                    try {
                        moneyPiont = Aes.decrypt(masterPassword, moneyPiont);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("aes_mly","---------------------"+e);
                    }
                    int pointInt=Integer.valueOf(moneyPiont)+200;
                    try {
                        moneyPiont = Aes.encrypt(masterPassword,pointInt+"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FileUtils.saveData(moneyPiont,"/Android/data/com.systemcamel.point","camel.txt");
                    Toast.makeText(SetAddPoint.this,"增加点数成功",Toast.LENGTH_LONG).show();
                    SetAddPoint.this.finish();
                }else {
                    DialogUtils.TipDialog(SetAddPoint.this, "请输入正确的兑换码", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    },"确定");
                }
            }
        });
    }

    private void initData(){
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        for (int i=0;i<100;i++){
            String pointStr1=(int)(Math.random()*1000000)+"";
            String pointStr2=(int)(Math.random()*1000000)+"";
            String pointStr=pointStr1+pointStr2;
            //AES加密
            String masterPassword = "tebiefuza";
            String encryptingCode = null;
            try {
                encryptingCode = Aes.encrypt(masterPassword,pointStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i==99){
                buffer.append(encryptingCode);
                buffer2.append(pointStr);
            }else {
                buffer.append(encryptingCode+"##");
                buffer2.append(pointStr+"##");
            }
        }

        Log.e("bufferPoint","---------------------"+buffer.toString());
        FileUtils.saveData(buffer.toString(),"/Android/data/com.systemcamel.point","camelPoint.txt");
//        FileUtils.saveData(buffer2.toString(),"/Android/data/com.systemcamel.point","camelPointNomal.txt");

    }
}
