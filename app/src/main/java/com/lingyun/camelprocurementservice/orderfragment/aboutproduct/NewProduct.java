package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseActivity;
import com.lingyun.camelprocurementservice.customview.pickerscrollview.PickerScrollView;
import com.lingyun.camelprocurementservice.customview.pickerscrollview.Pickers;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.FileUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static java.security.AccessController.getContext;

public class NewProduct extends BaseActivity {
    private PickerScrollView pickerscrlllview; // 滚动选择器
    private List<Pickers> list; // 滚动选择器数据
    private String[] name;//存放选择器内容的数组
    private String selectName;//被选中的内容
    private String selectButton;//当前点击的按钮
    private Button bt_yes, bt_no; // 确定,取消按钮
    private RelativeLayout picker_rel; // 选择器布局

    private EditText newproduct_name_et;
    private ImageView order_title_back, newproduct_name_iv, newproduct_image_iv,order_title_rightIv;
    private TextView newproduct_classify_tv, newproduct_cost_tv, newproduct_point_tv;
    private RelativeLayout newproduct_classify_rl, newproduct_cost_rl, newproduct_image_rl, newproduct_point_rl;
    private EditText newproduct_inPrice_et, newproduct_costPrice_et, newproduct_outPrice_et;
    private TextView newproduct_profitPrice_tv;
    private Button newproduct_btn;
    private String imageUrl;
    private Map map;
    private List<Map> productList;
    public static Handler handler;
    private String key;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        initView();
        initLinstener();
        initHandler();
    }

    /**
     * 初始化
     */
    private void initView() {
        //滚动选择器相关
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        bt_yes = (Button) findViewById(R.id.picker_yes);
        bt_no = (Button) findViewById(R.id.picker_no);

        //
        order_title_back = findViewById(R.id.order_title_back);
        newproduct_name_et = findViewById(R.id.newproduct_name_et);
        newproduct_name_iv = findViewById(R.id.newproduct_name_iv);
        newproduct_classify_tv = findViewById(R.id.newproduct_classify_tv);
        newproduct_classify_rl = findViewById(R.id.newproduct_classify_rl);
        newproduct_cost_tv = findViewById(R.id.newproduct_cost_tv);
        newproduct_cost_rl = findViewById(R.id.newproduct_cost_rl);
        newproduct_inPrice_et = findViewById(R.id.newproduct_inPrice_et);
        newproduct_costPrice_et = findViewById(R.id.newproduct_costPrice_et);
        newproduct_outPrice_et = findViewById(R.id.newproduct_outPrice_et);
        newproduct_profitPrice_tv = findViewById(R.id.newproduct_profitPrice_tv);
        newproduct_image_rl = findViewById(R.id.newproduct_image_rl);
        newproduct_image_iv = findViewById(R.id.newproduct_image_iv);
        newproduct_btn = findViewById(R.id.newproduct_btn);
        newproduct_point_rl = findViewById(R.id.newproduct_point_rl);
        newproduct_point_tv = findViewById(R.id.newproduct_point_tv);
        order_title_rightIv = findViewById(R.id.order_title_rightIv);
    }

    /**
     * 设置监听事件
     */
    private void initLinstener() {
        //滚动选择器相关
        pickerscrlllview.setOnSelectListener(pickerListener);
        bt_yes.setOnClickListener(onClickListener);
        bt_no.setOnClickListener(onClickListener);

        //
        order_title_back.setOnClickListener(onClickListener);
        newproduct_name_iv.setOnClickListener(onClickListener);
        newproduct_classify_rl.setOnClickListener(onClickListener);
        newproduct_cost_rl.setOnClickListener(onClickListener);
        newproduct_image_rl.setOnClickListener(onClickListener);
        newproduct_point_rl.setOnClickListener(onClickListener);
        newproduct_btn.setOnClickListener(onClickListener);
        order_title_rightIv.setOnClickListener(onClickListener);

        newproduct_outPrice_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profitPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        newproduct_inPrice_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profitPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        newproduct_costPrice_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                profitPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData(String value) {
        list = new ArrayList<Pickers>();
        if (value.equals("newproduct_cost_rl")) {
            name = new String[]{"人民币", "美元", "加元", "澳元", "欧元", "韩元", "日元", "英镑", "港元"};
        } else if (value.equals("newproduct_classify_rl")) {
            name = new String[]{"全部","现货", "母婴", "个护美妆", "营养保健", "钟表", "服装", "轻奢", "食品", "家居", "其它"};
        }
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], i + ""));
        }
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);
    }

    // 滚动选择器选中事件
    PickerScrollView.onSelectListener pickerListener = new PickerScrollView.onSelectListener() {

        @Override
        public void onSelect(Pickers pickers) {
            selectName = pickers.getShowConetnt();
        }
    };

    // 点击监听事件
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == newproduct_classify_rl) {
                hintKbTwo();
                initData("newproduct_classify_rl");
                selectButton = "newproduct_classify_rl";
                picker_rel.setVisibility(View.VISIBLE);
            } else if (v == newproduct_cost_rl) {
                initData("newproduct_cost_rl");
                selectButton = "newproduct_cost_rl";
                picker_rel.setVisibility(View.VISIBLE);
            } else if (v == bt_yes) {
                picker_rel.setVisibility(View.GONE);
                if (selectButton.equals("newproduct_classify_rl")) {
                    newproduct_classify_tv.setText(selectName);
                } else if (selectButton.equals("newproduct_cost_rl")) {
                    newproduct_cost_tv.setText(selectName);
                }
            } else if (v == bt_no) {
                picker_rel.setVisibility(View.GONE);
            } else if (v == order_title_back) {
                NewProduct.this.finish();
            } else if (v == newproduct_name_iv) {
                newproduct_name_et.setText("");
            } else if (v == newproduct_image_rl) {
                getSystemImage();
            }else if (v == order_title_rightIv) {
                showShare();
            } else if (v == newproduct_point_rl) {
                Intent intent = new Intent(NewProduct.this, CheckPoint.class);
                intent.putExtra("key", "newproduct");
                startActivity(intent);
            } else if (v == newproduct_btn) {
                map = new HashMap();
                String productId = (Math.random() * 100000000) + "";
                String proudctName = newproduct_name_et.getText().toString();
                String proudctClassify = newproduct_classify_tv.getText().toString();
                String proudctCurrency = newproduct_cost_tv.getText().toString();
                String proudctPoint = newproduct_point_tv.getText().toString();
                String proudctInPrice = newproduct_inPrice_et.getText().toString();
                String proudctCostPrice = newproduct_costPrice_et.getText().toString();
                String proudctOutPrice = newproduct_outPrice_et.getText().toString();
                String proudctProfiPrice = newproduct_profitPrice_tv.getText().toString();
                map.put("productId", productId);
                map.put("proudctName", proudctName);
                map.put("proudctClassify", proudctClassify);
                map.put("proudctCurrency", proudctCurrency);
                map.put("proudctPoint", proudctPoint);
                map.put("proudctInPrice", proudctInPrice);
                map.put("proudctCostPrice", proudctCostPrice);
                map.put("proudctOutPrice", proudctOutPrice);
                map.put("proudctProfiPrice", proudctProfiPrice);
                map.put("proudctImageUrl", imageUrl);

                productList = new ArrayList<>();
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = getSharedPreferences("Product", Context.MODE_PRIVATE);
                String midStr = sharedPreferences.getString("productList", "camel");
                if (midStr.equals("camel")) {
                    productList.clear();
                    productList.add(map);
                    String productStr = gson.toJson(productList);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("productList", productStr);
                    editor.commit();
                    Log.e("mly_productStr", "------------" + productStr);
                    Toast.makeText(NewProduct.this, "保存产品成功", Toast.LENGTH_LONG).show();
//                    ActivityManagerUtils.getInstance().finishActivityclass(CheckProduct.class);//先关闭之前的页面，避免重复创建
                    if ("checkproduct".equals(key)) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = gson.toJson(map);
                        NewOrderActivity.handler.sendMessage(message);
                    }
                    NewProduct.this.finish();
                } else {
                    boolean islife = false;
                    List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
                    }.getType());
                    productList.clear();
                    productList.addAll(midList);
                    productList.add(map);
                    String productStr = gson.toJson(productList);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("productList", productStr);
                    editor.commit();
                    Log.e("mly_productStr", "------------" + productStr);
                    Toast.makeText(NewProduct.this, "保存产品成功", Toast.LENGTH_LONG).show();
//                    ActivityManagerUtils.getInstance().finishActivityclass(CheckProduct.class);//先关闭之前的页面，避免重复创建
                    if ("checkproduct".equals(key)) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = gson.toJson(map);
                        NewOrderActivity.handler.sendMessage(message);
                    }
                    NewProduct.this.finish();

                }
            }
        }
    };

    //计算毛利
    private void profitPrice() {
        String inprice = newproduct_inPrice_et.getText().toString();
        String costprice = newproduct_costPrice_et.getText().toString();
        String outprice = newproduct_outPrice_et.getText().toString();

        if (inprice.equals("")) {
            inprice = "0.00";
        }
        if (costprice.equals("")) {
            costprice = "0.00";
        }
        if (outprice.equals("")) {
            outprice = "0.00";
        }

        float in = Float.parseFloat(inprice);
        float cost = Float.parseFloat(costprice);
        float out = Float.parseFloat(outprice);

        float result = out - cost - in;
        DecimalFormat df = new DecimalFormat("#########.##");
        String profit = df.format(result);
        newproduct_profitPrice_tv.setText(profit);

    }

    private void getSystemImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Uri localUri = data.getData();
            Uri mi=geturi(data);
            Log.e("imagePath_mly","-----------localUri-------------"+localUri.toString());
            Log.e("imagePath_mly","-------------mi-----------"+mi.toString());
//            showUploadImage(localUri.toString());
//            imageUrl = localUri.toString();

            String imagePath=FileUtils.getPath(NewProduct.this,localUri);//通过这个方法拿到图片在手机中的绝对路径

            FileUtils.makeRootDirectory("/sdcard/camelImage");//创建一个新文件夹
            String newPath=FileUtils.copyFile(NewProduct.this,imagePath,"/sdcard/camelImage");//将图片转移到新文件夹，并返回路径

            Log.e("imagePath_mly","------------------------"+newPath);
            showUploadImage(newPath);
            imageUrl=newPath;//这里改用图片的绝对路径替换uri
        }
    }

    private void showUploadImage(String imageUri) {
        if (imageUri != null) {
            Glide.with(this).load(imageUri).centerCrop().thumbnail(0.3f).into(newproduct_image_iv);
        }

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                String point = (String) msg.obj;
                newproduct_point_tv.setText(point);
            }
        };
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                }
                if (index == 0) {
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我是Title");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imageUrl);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
