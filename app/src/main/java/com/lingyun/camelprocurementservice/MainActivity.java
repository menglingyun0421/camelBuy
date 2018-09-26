package com.lingyun.camelprocurementservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.homepage.HomeActivity;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.NewClient;
import com.lingyun.camelprocurementservice.welcomepage.WelcomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager welcome_vp;
    private List<ImageView> listImg;
    private List<Integer> listImgId;
    private ImageView[] points;
    private int currentIndex;
    private Button welcome_btn_skip,welcome_btn_toHome;
    private boolean isComeIn=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        openPermissions();
        initView();
        initData();
        //初始化底部小点
        initPoint(listImgId.size());

        welcome_vp.setOnPageChangeListener(this);
        WelcomeAdapter adapter = new WelcomeAdapter(listImg);
        welcome_vp.setAdapter(adapter);

        //判断是否是第一次打开app
        SharedPreferences sharedPreferences = getSharedPreferences("isFistOpenApp", Context.MODE_PRIVATE);
        String isFist = sharedPreferences.getString("isFist", "YES");
        if (isFist=="YES"){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isFist", "NO");
            editor.commit();
        }else {
                startActivity(new Intent().setClass(MainActivity.this, HomeActivity.class));
                MainActivity.this.finish();
        }
    }

    //初始化控件
    private void initView() {
        welcome_vp = findViewById(R.id.welcome_vp);
        welcome_btn_skip=findViewById(R.id.welcome_btn_skip);
        welcome_btn_toHome=findViewById(R.id.welcome_btn_toHome);
        welcome_btn_skip.setOnClickListener(this);
        welcome_btn_toHome.setOnClickListener(this);
    }

    //初始化数据
    private void initData() {
        listImgId = new ArrayList<>();
        listImgId.add(R.mipmap.welcome_bg1);
        listImgId.add(R.mipmap.welcome_bg2);
        listImgId.add(R.mipmap.welcome_bg3);
        listImg = new ArrayList<ImageView>();
        for (int i = 0; i < listImgId.size(); i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(listImgId.get(i));
            listImg.add(iv);
        }
    }

    //vp的监听器
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //vp的监听器
    @Override
    public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
        if (position==2){
            welcome_btn_toHome.setVisibility(View.VISIBLE);
        }else {
            welcome_btn_toHome.setVisibility(View.GONE);
        }
    }

    //vp的监听器
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化底部小点
     */
    private void initPoint(int views) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.welcome_point);

        points = new ImageView[views];

        //循环取得小点图片
        for (int i = 0; i < views; i++) {
            //得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            //默认都设为灰色
            points[i].setEnabled(true);
            //给每个小点设置监听
            points[i].setOnClickListener(this);
            //设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > 2 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    /**
     * 按钮点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.welcome_btn_toHome:
                if (isComeIn){
                    intent.setClass(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }else {
                    Toast.makeText(this, "权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.welcome_btn_skip:
                if (isComeIn){
                    intent.setClass(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }else {
                    Toast.makeText(this, "权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    //Android6.0以后动态开启权限
    private void openPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//判断当前手机版本是否在6.0以上
            isComeIn=true;
        } else {
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//            int checkLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int checkRedPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int checkCamerPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (
//                    checkCallPhonePermission != PackageManager.PERMISSION_GRANTED ||
//                    checkLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    checkRedPermission != PackageManager.PERMISSION_GRANTED
//                    ||checkCamerPermission != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{
//                        Manifest.permission.CALL_PHONE,
//                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1000);
            } else {//已经开启权限
                isComeIn=true;
            }
        }
    }

    //权限申请回掉
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults != null && grantResults.length > 0) {
                    boolean permission = false;
//                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//电话权限
//                        permission = true;
//                        Toast.makeText(this, "电话权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
//                    }
//                    if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {//摄像头权限
//                        permission = true;
//                        Toast.makeText(this, "相机权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
//                    }
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//读写权限
                        permission = true;
                        Toast.makeText(this, "存储权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
                    }
//                    if (grantResults[3] != PackageManager.PERMISSION_GRANTED) {//定位权限
//                        permission = true;
//                        Toast.makeText(this, "定位权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
//                    }
                    if (permission) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                        startActivity(intent);
                    } else {
                        isComeIn=true;
                    }
                } else {
                    Toast.makeText(this, "权限开始失败，请到手机设置中开启权限。", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

}
