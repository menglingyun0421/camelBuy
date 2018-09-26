package com.lingyun.camelprocurementservice.orderfragment.neworder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import com.lingyun.camelprocurementservice.homepage.HomeActivity;
import com.lingyun.camelprocurementservice.orderfragment.OrderDetail;
import com.lingyun.camelprocurementservice.orderfragment.aboutAddress.CheckAddress;
import com.lingyun.camelprocurementservice.orderfragment.aboutRemark.EditRemark;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.ClientNameAdapter;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckPoint;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckProduct;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.NewProduct;
import com.lingyun.camelprocurementservice.utils.ActivityManagerUtils;
import com.lingyun.camelprocurementservice.utils.Aes;
import com.lingyun.camelprocurementservice.utils.DialogUtils;
import com.lingyun.camelprocurementservice.utils.FileUtils;
import com.lingyun.camelprocurementservice.utils.OrderDBHelperUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrderActivity extends BaseActivity {
    private PickerScrollView pickerscrlllview; // 滚动选择器
    private List<Pickers> list; // 滚动选择器数据
    private String[] name;//存放选择器内容的数组
    private String selectName;//被选中的内容
    private Button bt_yes, bt_no; // 确定,取消按钮
    private RelativeLayout picker_rel; // 选择器布局

    private RelativeLayout neworder_client, neworder_product;
    private String username, address, remark;
    private Map productMap;
    private TextView neworder_clientName;
    private ImageView order_title_leftIv;
    public static Handler handler;
    private ImageView neworder_product_iv;
    private TextView neworder_productName;
    private RelativeLayout neworder_point;
    private TextView neworder_point_tv;
    private EditText neworder_inprice_val, neworder_costprice_val, neworder_outprice_val;
    private TextView neworder_count_tv;
    private Button neworder_count_sub, neworder_count_add;
    private float productCount = 1f;
    private RelativeLayout neworder_received, neworder_stock, neworder_payment, neworder_deliver,neworder_harvest;
    private String isPayment = "false", isStock = "false", isReceived = "false", isDeliver = "false",isHarvest="false";
    private ImageView received_cheked, received_normal, stock_cheked, stock_normal, payment_cheked, payment_normal, deliver_cheked, deliver_normal,harvest_cheked,harvest_normal;
    private TextView neworder_totalprice, neworder_totalprofit;
    private RelativeLayout neworder_distribution, neworder_address, neworder_remark;
    private TextView neworder_distribution_tv;
    private TextView neworder_address_tv, neworder_remark_tv;
    private Button neworder_save;
    private Map map;
    private String midClassify, midCurrency, imageUrl;
    private List orderList;
    private EditText neworder_ExpressNumber_val;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        initView();
        initLinstener();
        initData();
        handler();

    }

    /**
     * 初始化
     */
    @SuppressLint("WrongViewCast")
    private void initView() {
        //滚动选择器相关
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        bt_yes = (Button) findViewById(R.id.picker_yes);
        bt_no = (Button) findViewById(R.id.picker_no);

        neworder_client = findViewById(R.id.neworder_client);
        neworder_clientName = findViewById(R.id.neworder_clientName);
        order_title_leftIv = findViewById(R.id.order_title_leftIv);
        neworder_product = findViewById(R.id.neworder_product);
        neworder_product_iv = findViewById(R.id.neworder_product_iv);
        neworder_productName = findViewById(R.id.neworder_productName);
        neworder_point = findViewById(R.id.neworder_point);
        neworder_point_tv = findViewById(R.id.neworder_point_tv);
        neworder_inprice_val = findViewById(R.id.neworder_inprice_val);
        neworder_costprice_val = findViewById(R.id.neworder_costprice_val);
        neworder_outprice_val = findViewById(R.id.neworder_outprice_val);
        neworder_count_sub = findViewById(R.id.neworder_count_sub);
        neworder_count_add = findViewById(R.id.neworder_count_add);
        neworder_count_tv = findViewById(R.id.neworder_count_tv);
        neworder_received = findViewById(R.id.neworder_received);
        neworder_stock = findViewById(R.id.neworder_stock);
        neworder_payment = findViewById(R.id.neworder_payment);
        neworder_deliver = findViewById(R.id.neworder_deliver);
        received_cheked = findViewById(R.id.received_cheked);
        received_normal = findViewById(R.id.received_normal);
        stock_cheked = findViewById(R.id.stock_cheked);
        stock_normal = findViewById(R.id.stock_normal);
        payment_cheked = findViewById(R.id.payment_cheked);
        payment_normal = findViewById(R.id.payment_normal);
        deliver_cheked = findViewById(R.id.deliver_cheked);
        deliver_normal = findViewById(R.id.deliver_normal);
        neworder_totalprice = findViewById(R.id.neworder_totalprice);
        neworder_totalprofit = findViewById(R.id.neworder_totalprofit);
        neworder_distribution = findViewById(R.id.neworder_distribution);
        neworder_address = findViewById(R.id.neworder_address);
        neworder_remark = findViewById(R.id.neworder_remark);
        neworder_distribution_tv = findViewById(R.id.neworder_distribution_tv);
        neworder_address_tv = findViewById(R.id.neworder_address_tv);
        neworder_remark_tv = findViewById(R.id.neworder_remark_tv);
        neworder_save = findViewById(R.id.neworder_save);
        neworder_ExpressNumber_val = findViewById(R.id.neworder_ExpressNumber_val);
        neworder_harvest = findViewById(R.id.neworder_harvest);
        harvest_cheked = findViewById(R.id.harvest_cheked);
        harvest_normal = findViewById(R.id.harvest_normal);
    }

    /**
     * 设置监听事件
     */
    private void initLinstener() {
        //滚动选择器相关
        pickerscrlllview.setOnSelectListener(pickerListener);
        bt_yes.setOnClickListener(onClickListener);
        bt_no.setOnClickListener(onClickListener);
        //点击跳转到选择客户页面
        neworder_client.setOnClickListener(onClickListener);
        //返回上一个页面
        order_title_leftIv.setOnClickListener(onClickListener);
        //点击跳转到选择商品页面
        neworder_product.setOnClickListener(onClickListener);
        //增加、减少商品数量
        neworder_count_sub.setOnClickListener(onClickListener);
        neworder_count_add.setOnClickListener(onClickListener);
        //已收款、备货、付款、发货
        neworder_received.setOnClickListener(onClickListener);
        neworder_stock.setOnClickListener(onClickListener);
        neworder_payment.setOnClickListener(onClickListener);
        neworder_deliver.setOnClickListener(onClickListener);
        //递送方式
        neworder_distribution.setOnClickListener(onClickListener);
        //收获地址
        neworder_address.setOnClickListener(onClickListener);
        //备注
        neworder_remark.setOnClickListener(onClickListener);
        //保存
        neworder_save.setOnClickListener(onClickListener);
        //采购点
        neworder_point.setOnClickListener(onClickListener);
        //是否收获
        neworder_harvest.setOnClickListener(onClickListener);

        neworder_inprice_val.addTextChangedListener(new TextWatcher() {
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
        neworder_costprice_val.addTextChangedListener(new TextWatcher() {
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
        neworder_outprice_val.addTextChangedListener(new TextWatcher() {
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
    private void initData() {
        list = new ArrayList<Pickers>();
        name = new String[]{"直邮", "人肉", "拼箱", "国内现货"};
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
            if (v == neworder_distribution) {
                picker_rel.setVisibility(View.VISIBLE);
            } else if (v == bt_yes) {
                picker_rel.setVisibility(View.GONE);
//                Toast.makeText(NewOrderActivity.this,selectName,Toast.LENGTH_LONG).show();
                neworder_distribution_tv.setText(selectName);
            } else if (v == bt_no) {
                picker_rel.setVisibility(View.GONE);
            } else if (v == neworder_client) {
                startActivity(new Intent().setClass(NewOrderActivity.this, CheckClient.class));
            } else if (v == order_title_leftIv) {
                NewOrderActivity.this.finish();
            } else if (v == neworder_product) {
                startActivity(new Intent().setClass(NewOrderActivity.this, CheckProduct.class));
            } else if (v == neworder_count_sub) {
                if (1 < productCount) {
                    productCount--;
                }
                neworder_count_tv.setText(productCount + "");
                profitPrice();
            } else if (v == neworder_count_add) {
                productCount++;
                neworder_count_tv.setText(productCount + "");
                profitPrice();
            } else if (v == neworder_received) {
                if (isReceived.equals("false")) {
                    received_cheked.setVisibility(View.VISIBLE);
                    received_normal.setVisibility(View.GONE);
                    isReceived = "true";
                } else if (isReceived.equals("true")) {
                    received_cheked.setVisibility(View.GONE);
                    received_normal.setVisibility(View.VISIBLE);
                    isReceived = "false";
                }
            } else if (v == neworder_stock) {
                if (isStock.equals("false")) {
                    stock_cheked.setVisibility(View.VISIBLE);
                    stock_normal.setVisibility(View.GONE);
                    isStock = "true";
                } else if (isStock.equals("true")) {
                    stock_cheked.setVisibility(View.GONE);
                    stock_normal.setVisibility(View.VISIBLE);
                    isStock = "false";
                }
            } else if (v == neworder_payment) {
                if (isPayment.equals("false")) {
                    payment_cheked.setVisibility(View.VISIBLE);
                    payment_normal.setVisibility(View.GONE);
                    isPayment = "true";
                } else if (isPayment.equals("true")) {
                    payment_cheked.setVisibility(View.GONE);
                    payment_normal.setVisibility(View.VISIBLE);
                    isPayment = "false";
                }
            }else if (v == neworder_harvest) {
                if (isHarvest.equals("false")) {
                    harvest_cheked.setVisibility(View.VISIBLE);
                    harvest_normal.setVisibility(View.GONE);
                    isHarvest = "true";
                } else if (isHarvest.equals("true")) {
                    harvest_cheked.setVisibility(View.GONE);
                    harvest_normal.setVisibility(View.VISIBLE);
                    isHarvest = "false";
                }
            } else if (v == neworder_deliver) {
                if (isDeliver.equals("false")) {
                    deliver_cheked.setVisibility(View.VISIBLE);
                    deliver_normal.setVisibility(View.GONE);
                    isDeliver = "true";
                } else if (isDeliver.equals("true")) {
                    deliver_cheked.setVisibility(View.GONE);
                    deliver_normal.setVisibility(View.VISIBLE);
                    isDeliver = "false";
                }
            } else if (v == neworder_address) {
//                startActivity(new Intent().setClass(NewOrderActivity.this, CheckAddress.class));
                Intent intent = new Intent();
                intent.putExtra("key", "newOrder");
                intent.putExtra("clientName", neworder_clientName.getText().toString());
                intent.setClass(NewOrderActivity.this, CheckAddress.class);
                startActivity(intent);
            }else if (v == neworder_point) {
//                startActivity(new Intent().setClass(NewOrderActivity.this, CheckAddress.class));
                Intent intent = new Intent();
                intent.putExtra("key", "newOrder");
                intent.setClass(NewOrderActivity.this, CheckPoint.class);
                startActivity(intent);
            } else if (v == neworder_remark) {
//                startActivity(new Intent().setClass(NewOrderActivity.this, EditRemark.class));
                Intent intent = new Intent();
                intent.putExtra("key", "newOrder");
                intent.setClass(NewOrderActivity.this, EditRemark.class);
                startActivity(intent);
            } else if (v == neworder_save) {
                String moneyPiont= FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
                //AES解密
                String masterPassword = "tebiefuza";
                String decryptingCode = null;
                try {
                    decryptingCode = Aes.decrypt(masterPassword, moneyPiont);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("aes_mly","---------------------"+e);
                }
                if (decryptingCode.equals("0")){
                    DialogUtils.TipDialog(NewOrderActivity.this, "体验点数以经用完，请购买新的点数", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    },"确定");
                }else {
                    save();
                }
            }
        }
    };

    private void handler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case 1:
                        username = (String) msg.obj;
                        neworder_clientName.setText(username);
                        initAddress(username);
                        break;
                    case 2:
                        Gson gson = new Gson();
                        String midStr = (String) msg.obj;
                        productMap = gson.fromJson(midStr, new TypeToken<Map<String, Object>>() {
                        }.getType());
                        Glide.with(NewOrderActivity.this).load((String) productMap.get("proudctImageUrl")).centerCrop().thumbnail(0.3f).into(neworder_product_iv);
                        neworder_productName.setText((String) productMap.get("proudctName"));
                        neworder_point_tv.setText((String) productMap.get("proudctPoint"));
                        neworder_inprice_val.setText((String) productMap.get("proudctInPrice"));
                        neworder_costprice_val.setText((String) productMap.get("proudctCostPrice"));
                        neworder_outprice_val.setText((String) productMap.get("proudctOutPrice"));
                        midClassify = ((String) productMap.get("proudctClassify"));
                        midCurrency = ((String) productMap.get("proudctCurrency"));
                        imageUrl = ((String) productMap.get("proudctImageUrl"));
                        break;
                    case 3:
                        address = (String) msg.obj;
                        neworder_address_tv.setText(address);
                        break;
                    case 4:
                        remark = (String) msg.obj;
                        neworder_remark_tv.setText(remark);
                        break;
                    case 5:
                        String pointStr = (String) msg.obj;
                        neworder_point_tv.setText(pointStr);
                        break;
                }
            }
        };
    }

    //计算毛利
    private void profitPrice() {
        String inprice = neworder_inprice_val.getText().toString();
        String costprice = neworder_costprice_val.getText().toString();
        String outprice = neworder_outprice_val.getText().toString();

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

        float resultProfit = (out - cost - in) * productCount;
        float resultPrice = out * productCount;
        DecimalFormat df = new DecimalFormat("#########.##");
        String profit = df.format(resultProfit);
        String price = df.format(resultPrice);
        neworder_totalprice.setText(price);
        neworder_totalprofit.setText(profit);

    }

    private void save() {

        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
//            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date(time);
        String creatTime = format.format(d1);

        map = new HashMap();
        String productId=(int)(Math.random() * 100000000)+"";
        String clientName = neworder_clientName.getText().toString();
        String proudctName = neworder_productName.getText().toString();
        String proudctClassify = midClassify;
        String proudctCurrency = midCurrency;
        String proudctPoint = neworder_point_tv.getText().toString();
        String proudctImageUrl = imageUrl;
        String proudctInPrice = neworder_inprice_val.getText().toString();
        String proudctCostPrice = neworder_costprice_val.getText().toString();
        String proudctOutPrice = neworder_outprice_val.getText().toString();
        String proudctCount = neworder_count_tv.getText().toString();
        String proudctIsReceived = isReceived;
        String proudctIsStock = isStock;
        String proudctIsPayment = isPayment;
        String proudctIsDeliver = isDeliver;
        String proudctIsHarvest = isHarvest;
        String proudctDistribution = neworder_distribution_tv.getText().toString();
        String proudctAddress = neworder_address_tv.getText().toString();
        String proudctRemark = neworder_remark_tv.getText().toString();
        String proudctTotalprice = neworder_totalprice.getText().toString();
        String proudctTotalprofit = neworder_totalprofit.getText().toString();
        String proudctExpressNumber = neworder_ExpressNumber_val.getText().toString();
        map.put("productId", productId);
        map.put("creatTime", creatTime);
        map.put("clientName", clientName);
        map.put("proudctName", proudctName);
        map.put("proudctClassify", proudctClassify);
        map.put("proudctCurrency", proudctCurrency);
        map.put("proudctPoint", proudctPoint);
        map.put("proudctImageUrl", proudctImageUrl);
        map.put("proudctInPrice", proudctInPrice);
        map.put("proudctCostPrice", proudctCostPrice);
        map.put("proudctOutPrice", proudctOutPrice);
        map.put("proudctCount", proudctCount);
        map.put("proudctIsReceived", proudctIsReceived);
        map.put("proudctIsStock", proudctIsStock);
        map.put("proudctIsPayment", proudctIsPayment);
        map.put("proudctIsDeliver", proudctIsDeliver);
        map.put("proudctIsHarvest", proudctIsHarvest);
        map.put("proudctDistribution", proudctDistribution);
        map.put("proudctAddress", proudctAddress);
        map.put("proudctRemark", proudctRemark);
        map.put("proudctTotalprice", proudctTotalprice);
        map.put("proudctTotalprofit", proudctTotalprofit);
        map.put("proudctExpressNumber", proudctExpressNumber);

        initDB ();//将数据存入数据库

        if (checkNull(clientName, proudctName)) {
            return;
        } else {
            orderList = new ArrayList<>();
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
            String midStr = sharedPreferences.getString("orderList", "camel");
            if (midStr.equals("camel")) {
                orderList.clear();
                orderList.add(map);
                String productStr = gson.toJson(orderList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("orderList", productStr);
                editor.commit();
                Log.e("mly_orderStr", "------------" + productStr);
                Toast.makeText(NewOrderActivity.this, "保存订单成功", Toast.LENGTH_LONG).show();
                //解密，将订单点数减一，然后加密，保存
                String moneyPiont= FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
                String masterPassword = "tebiefuza";
                String decryptingCode = null;
                try {
                    decryptingCode = Aes.decrypt(masterPassword, moneyPiont);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("aes_mly","---------------------"+e);
                }
                int money=Integer.valueOf(decryptingCode);
                money--;
                //AES加密
                String encryptingCode = null;
                try {
                    encryptingCode = Aes.encrypt(masterPassword,money+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileUtils.saveData(encryptingCode,"/Android/data/com.systemcamel.point","camel.txt");
//                    ActivityManagerUtils.getInstance().finishActivityclass(CheckProduct.class);//先关闭之前的页面，避免重复创建
//            Message message=new Message();
//            message.what=2;
//            message.obj=gson.toJson(map);
//            NewOrderActivity.handler.sendMessage(message);
                NewOrderActivity.this.finish();
            } else {
                List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
                }.getType());
                orderList.clear();
                orderList.addAll(midList);
                orderList.add(map);
                String productStr = gson.toJson(orderList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("orderList", productStr);
                editor.commit();
                Log.e("mly_orderStr", "------------" + productStr);
                Toast.makeText(NewOrderActivity.this, "保存订单成功", Toast.LENGTH_LONG).show();
                //解密，将订单点数减一，然后加密，保存
                String moneyPiont= FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
                String masterPassword = "tebiefuza";
                String decryptingCode = null;
                try {
                    decryptingCode = Aes.decrypt(masterPassword, moneyPiont);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("aes_mly","---------------------"+e);
                }
                int money=Integer.valueOf(decryptingCode);
                money--;
                //AES加密
                String encryptingCode = null;
                try {
                    encryptingCode = Aes.encrypt(masterPassword,money+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileUtils.saveData(encryptingCode,"/Android/data/com.systemcamel.point","camel.txt");
//                    ActivityManagerUtils.getInstance().finishActivityclass(CheckProduct.class);//先关闭之前的页面，避免重复创建
//            Message message=new Message();
//            message.what=2;
//            message.obj=gson.toJson(map);
//            NewOrderActivity.handler.sendMessage(message);
                NewOrderActivity.this.finish();
            }
        }

    }

    private boolean checkNull(String clientName, String name) {
        if (clientName.equals("请选择客户") || clientName.equals("")) {
            Toast.makeText(NewOrderActivity.this, "请填写客户名", Toast.LENGTH_LONG).show();
            return true;
        } else if (name.equals("请选择商品") || name.equals("")) {
            Toast.makeText(NewOrderActivity.this, "请填写商品名", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }
    }

    private void initAddress(String spclientname) {
        SharedPreferences sharedPreferences = getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(spclientname, "camel");
        Gson gson = new Gson();
        if (address.equals("camel")) {
            neworder_address_tv.setText("");
            return;
        } else {
            List<Map> addressList = gson.fromJson(address, new TypeToken<List<Map>>() {
            }.getType());
            Log.e("mly_useraddress", "------------" + addressList);
            String midStr = (String) addressList.get(0).get("name");
            neworder_address_tv.setText(midStr);
        }
    }

    //后续根据需要再这个地方新建了一个数据库，用来存储数据，方便后面将数据一Excel的形式导出
    private void initDB (){
        OrderDBHelperUtils orderDBHelperUtils=
                new OrderDBHelperUtils(NewOrderActivity.this,"order_table",null,1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db =orderDBHelperUtils.getReadableDatabase();

        // 数据库版本的更新,由原来的1变为2,更新的方法暂时用不到
//        OrderDBHelperUtils dbHelper = new OrderDBHelperUtils(NewOrderActivity.this,"stu_db",null,2);
//        SQLiteDatabase db =dbHelper.getReadableDatabase();

        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式
        cv.put("productId", (String) map.get("productId"));
        cv.put("creatTime", (String) map.get("creatTime"));
        cv.put("clientName", (String) map.get("clientName"));
        cv.put("proudctName", (String) map.get("proudctName"));
        cv.put("proudctClassify", (String) map.get("proudctClassify"));
        cv.put("proudctCurrency", (String) map.get("proudctCurrency"));
        cv.put("proudctPoint", (String) map.get("proudctPoint"));
        cv.put("proudctImageUrl", (String) map.get("proudctImageUrl"));
        cv.put("proudctInPrice", (String) map.get("proudctInPrice"));
        cv.put("proudctCostPrice", (String) map.get("proudctCostPrice"));
        cv.put("proudctOutPrice", (String) map.get("proudctOutPrice"));
        cv.put("proudctCount", (String) map.get("proudctCount"));
        cv.put("proudctIsReceived", (String) map.get("proudctIsReceived"));
        cv.put("proudctIsStock", (String) map.get("proudctIsStock"));
        cv.put("proudctIsPayment", (String) map.get("proudctIsPayment"));
        cv.put("proudctIsDeliver", (String) map.get("proudctIsDeliver"));
        cv.put("proudctIsHarvest", (String) map.get("proudctIsHarvest"));
        cv.put("proudctDistribution", (String) map.get("proudctDistribution"));
        cv.put("proudctAddress", (String) map.get("proudctAddress"));
        cv.put("proudctExpressNumber", (String) map.get("proudctExpressNumber"));
        cv.put("proudctTotalprice", (String) map.get("proudctTotalprice"));
        cv.put("proudctTotalprofit", (String) map.get("proudctTotalprofit"));
        cv.put("proudctRemark", (String) map.get("proudctRemark"));

        //调用insert方法，将数据插入数据库
        db.insert("order_table", null, cv);
        //关闭数据库
        db.close();
    }
}
