package com.lingyun.camelprocurementservice.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.clientfragment.ClientFragment;
import com.lingyun.camelprocurementservice.orderfragment.OrderFragment;
import com.lingyun.camelprocurementservice.productfragment.ProductFragment;
import com.lingyun.camelprocurementservice.setfragment.SetFragment;
import com.lingyun.camelprocurementservice.statisticsfragment.StatisticsFragment;
import com.lingyun.camelprocurementservice.utils.Aes;
import com.lingyun.camelprocurementservice.utils.FileUtils;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    private static boolean isExit=false;
    private RadioGroup rgBottom;
    private FragmentManager manager;//v4下的Fragment
    private Fragment[] fragments;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //获取fragment管理器
//        manager = getSupportFragmentManager();
        initFragment();
        initView();
        getMoney();
    }

    /**
     * 初始化Fragment，放置到数组中
     */
    private void initFragment() {
        fragments = new Fragment[5];

        OrderFragment orderFragment = new OrderFragment();
        fragments[0] = orderFragment;

        ClientFragment clientFragment = new ClientFragment();
        fragments[1] = clientFragment;

        ProductFragment productFragment = new ProductFragment();
        fragments[2] = productFragment;

        StatisticsFragment statisticsFragment = new StatisticsFragment();
        fragments[3] = statisticsFragment;

        SetFragment setFragment = new SetFragment();
        fragments[4] = setFragment;

    }

    /**
     * 初始化底部导航栏
     */
    private void initView() {
        rgBottom = (RadioGroup) findViewById(R.id.home_rg);
        rgBottom.setOnCheckedChangeListener(this);
        //设置第一个默认选中
        ((RadioButton) rgBottom.getChildAt(0)).setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentManager manager= getSupportFragmentManager();
        //导航实现fragment切换
        FragmentTransaction transaction = manager.beginTransaction();
        switch (i){
            case R.id.home_rb_order:
                transaction.replace(R.id.activity_main_container,fragments[0]);
                break;
            case R.id.home_rb_client:
                transaction.replace(R.id.activity_main_container,fragments[1]);
                break;
            case R.id.home_rb_product:
                transaction.replace(R.id.activity_main_container,fragments[2]);
                break;
            case R.id.home_rb_statistics:
                transaction.replace(R.id.activity_main_container,fragments[3]);
                break;
            case R.id.home_rb_set:
                transaction.replace(R.id.activity_main_container,fragments[4]);
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==2){
            initFragment();
            FragmentManager manager= getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.activity_main_container,fragments[1]);
            transaction.commit();
        }
    }

    private void getMoney (){
        boolean isMk=FileUtils.makeRootDirectory("/sdcard/Android/data/com.systemcamel.point");
        if (isMk){
            String moneyPiont=FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
            Log.e("moneyPiont","---------------"+moneyPiont);
        }else {
            //AES加密
            String masterPassword = "tebiefuza";
            String originalText = "200";
            String encryptingCode = null;
            try {
                encryptingCode = Aes.encrypt(masterPassword,originalText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtils.saveData(encryptingCode,"/Android/data/com.systemcamel.point","camel.txt");
        }


    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
