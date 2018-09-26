
package com.lingyun.camelprocurementservice.setfragment;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
            Log.i("dir",Environment.getExternalStorageDirectory().toString());
            File saveFile = new File(sdCardDir,"a.txt");

            //写数据
            try {
                FileOutputStream fos= new FileOutputStream(saveFile);
                fos.write("bobobo_shaka_laka".getBytes());
                fos.close();
                Toast.makeText(SetActivity.this,"保存数据成功",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SetActivity.this,"保存数据失败",Toast.LENGTH_LONG).show();
            }

            //读数据
            try {
                FileInputStream fis= new FileInputStream(saveFile);
                int len =0;
                byte[] buf = new byte[1024];
                StringBuffer sb = new StringBuffer();
                while((len=fis.read(buf))!=-1){
                    sb.append(new String(buf, 0, len));
                }
//                Toast.makeText(SetActivity.this,sb.toString(),Toast.LENGTH_LONG).show();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
