package com.lingyun.camelprocurementservice.orderfragment;

import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import com.lingyun.camelprocurementservice.orderfragment.aboutAddress.CheckAddress;
import com.lingyun.camelprocurementservice.orderfragment.aboutRemark.EditRemark;
import com.lingyun.camelprocurementservice.orderfragment.aboutclient.CheckClient;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckPoint;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.CheckProduct;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.DialogUtils;
import com.lingyun.camelprocurementservice.utils.OrderDBHelperUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetail extends BaseActivity {
    public static Handler handler;
    private Map map;
    private ImageView order_title_leftIv;
    private TextView neworder_unreceived, neworder_unstock, neworder_unpayment, neworder_time_val, neworder_unDeliver,neworder_unHarvest;
    private TextView neworder_clientName, neworder_productName;
    private ImageView neworder_product_iv,neworder_address_iv;
    private EditText neworder_inprice_val, neworder_costprice_val, neworder_outprice_val;
    private TextView neworder_count_tv, neworder_point_tv;
    private float productCount = 1f;
    private Button neworder_count_sub, neworder_count_add;
    private RelativeLayout neworder_received, neworder_stock, neworder_payment, neworder_deliver, neworder_point, neworder_harvest;
    private String isPayment, isStock, isReceived, isDeliver, isHarvest;
    private ImageView received_cheked, received_normal, stock_cheked, stock_normal, payment_cheked, payment_normal, deliver_cheked, deliver_normal, harvest_cheked, harvest_normal;
    private RelativeLayout neworder_distribution, neworder_address, neworder_remark;
    private TextView neworder_distribution_tv;
    private TextView neworder_address_tv, neworder_remark_tv,neworder_address_tv_copy;
    private RelativeLayout neworder_delete;
    private TextView neworder_totalprice, neworder_totalprofit;
    private Button neworder_save;
    private RelativeLayout neworder_save_rl;
    private String midClassify, midCurrency, imageUrl, creatTime, beginProductId;
    private List productList;
    private EditText neworder_ExpressNumber_val;

    private PickerScrollView pickerscrlllview; // 滚动选择器
    private List<Pickers> list; // 滚动选择器数据
    private String[] name;//存放选择器内容的数组
    private String selectName;//被选中的内容
    private Button bt_yes, bt_no; // 确定,取消按钮
    private RelativeLayout picker_rel; // 选择器布局

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        Gson gson = new Gson();
        map = gson.fromJson(order, new TypeToken<Map<String, Object>>() {
        }.getType());

        initView();
        initLinstener();
        initData();
        tohandler();
    }

    private void initView() {
        order_title_leftIv = findViewById(R.id.order_title_leftIv);
        neworder_unreceived = findViewById(R.id.neworder_unreceived);
        neworder_unstock = findViewById(R.id.neworder_unstock);
        neworder_unpayment = findViewById(R.id.neworder_unpayment);
        neworder_unDeliver = findViewById(R.id.neworder_unDeliver);
        neworder_time_val = findViewById(R.id.neworder_time_val);
        neworder_clientName = findViewById(R.id.neworder_clientName);
        neworder_point_tv = findViewById(R.id.neworder_point_tv);
        neworder_productName = findViewById(R.id.neworder_productName);
        neworder_product_iv = findViewById(R.id.neworder_product_iv);
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
        neworder_distribution = findViewById(R.id.neworder_distribution);
        neworder_address = findViewById(R.id.neworder_address);
        neworder_remark = findViewById(R.id.neworder_remark);
        neworder_distribution_tv = findViewById(R.id.neworder_distribution_tv);
        neworder_address_tv = findViewById(R.id.neworder_address_tv);
        neworder_remark_tv = findViewById(R.id.neworder_remark_tv);
        neworder_delete = findViewById(R.id.neworder_delete);
        neworder_totalprice = findViewById(R.id.neworder_totalprice);
        neworder_totalprofit = findViewById(R.id.neworder_totalprofit);
        neworder_save = findViewById(R.id.neworder_save);
        neworder_save_rl = findViewById(R.id.neworder_save_rl);
        neworder_ExpressNumber_val = findViewById(R.id.neworder_ExpressNumber_val);
        neworder_point = findViewById(R.id.neworder_point);
        neworder_harvest = findViewById(R.id.neworder_harvest);
        harvest_cheked = findViewById(R.id.harvest_cheked);
        harvest_normal = findViewById(R.id.harvest_normal);
        neworder_unHarvest = findViewById(R.id.neworder_unHarvest);
        neworder_address_iv = findViewById(R.id.neworder_address_iv);
        neworder_address_tv_copy = findViewById(R.id.neworder_address_tv_copy);

        //滚动选择器相关
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        bt_yes = (Button) findViewById(R.id.picker_yes);
        bt_no = (Button) findViewById(R.id.picker_no);
    }

    private void initLinstener() {
        //滚动选择器相关
        pickerscrlllview.setOnSelectListener(pickerListener);
        bt_yes.setOnClickListener(onClickListener);
        bt_no.setOnClickListener(onClickListener);

        order_title_leftIv.setOnClickListener(onClickListener);
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
//        neworder_address.setOnClickListener(onClickListener);
        neworder_address_tv.setOnClickListener(onClickListener);
        neworder_address_iv.setOnClickListener(onClickListener);
        neworder_address_tv_copy.setOnClickListener(onClickListener);
        //备注
        neworder_remark.setOnClickListener(onClickListener);
        //删除
        neworder_delete.setOnClickListener(onClickListener);
        //保存
        neworder_save.setOnClickListener(onClickListener);
        neworder_save_rl.setOnClickListener(onClickListener);
        //
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

    private void initData() {
        list = new ArrayList<Pickers>();
        name = new String[]{"直邮", "人肉", "拼箱", "国内现货"};
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], i + ""));
        }
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);

        String unreceived = (String) map.get("proudctIsReceived");
        String unstock = (String) map.get("proudctIsStock");
        String unpayment = (String) map.get("proudctIsPayment");
        String unDeliver = (String) map.get("proudctIsDeliver");
        String unHarvest = (String) map.get("proudctIsHarvest");
        if (unreceived.equals("true")) {
//            neworder_unreceived.setVisibility(View.GONE);
            neworder_unreceived.setText("已收款");
            neworder_unreceived.setTextColor(Color.GREEN);
        }
        if (unstock.equals("true")) {
//            neworder_unstock.setVisibility(View.GONE);
            neworder_unstock.setText("已备货");
            neworder_unstock.setTextColor(Color.GREEN);
        }
        if (unpayment.equals("true")) {
//            neworder_unpayment.setVisibility(View.GONE);
            neworder_unpayment.setText("已收款");
            neworder_unpayment.setTextColor(Color.GREEN);
        }
        if (unDeliver.equals("true")) {
//            neworder_unDeliver.setVisibility(View.GONE);
            neworder_unDeliver.setText("已发货");
            neworder_unDeliver.setTextColor(Color.GREEN);
        }
        if ("true".equals(unHarvest)) {
//            neworder_unDeliver.setVisibility(View.GONE);
            neworder_unHarvest.setText("已收货");
            neworder_unHarvest.setTextColor(Color.GREEN);
        }
        if (unreceived.equals("true") && unstock.equals("true") && unpayment.equals("true") && unDeliver.equals("true")&& "true".equals(unHarvest)) {
            neworder_unDeliver.setVisibility(View.GONE);
            neworder_unpayment.setVisibility(View.GONE);
            neworder_unstock.setVisibility(View.GONE);
            neworder_unreceived.setVisibility(View.GONE);
            neworder_unHarvest.setVisibility(View.VISIBLE);
            neworder_unHarvest.setText("完成");
            neworder_unHarvest.setTextColor(Color.BLACK);
        }
        neworder_time_val.setText((String) map.get("creatTime"));
        creatTime = (String) map.get("creatTime");
        neworder_clientName.setText((String) map.get("clientName"));
        neworder_productName.setText((String) map.get("proudctName"));
        neworder_point_tv.setText((String) map.get("proudctPoint"));
        Glide.with(OrderDetail.this).load((String) map.get("proudctImageUrl")).centerCrop().thumbnail(0.3f).into(neworder_product_iv);
        neworder_inprice_val.setText((String) map.get("proudctInPrice"));
        neworder_costprice_val.setText((String) map.get("proudctCostPrice"));
        neworder_outprice_val.setText((String) map.get("proudctOutPrice"));
        isReceived = (String) map.get("proudctIsReceived");
        isStock = (String) map.get("proudctIsStock");
        isPayment = (String) map.get("proudctIsPayment");
        isDeliver = (String) map.get("proudctIsDeliver");
        isHarvest = (String) map.get("proudctIsHarvest");
        midClassify = ((String) map.get("proudctClassify"));
        beginProductId = ((String) map.get("productId"));
        midCurrency = ((String) map.get("proudctCurrency"));
        imageUrl = ((String) map.get("proudctImageUrl"));
        if (isReceived.equals("true")) {
            received_cheked.setVisibility(View.VISIBLE);
            received_normal.setVisibility(View.GONE);
            isReceived = "true";
        } else if (isReceived.equals("false")) {
            received_cheked.setVisibility(View.GONE);
            received_normal.setVisibility(View.VISIBLE);
            isReceived = "false";
        }
        if (isStock.equals("true")) {
            stock_cheked.setVisibility(View.VISIBLE);
            stock_normal.setVisibility(View.GONE);
            isStock = "true";
        } else if (isStock.equals("false")) {
            stock_cheked.setVisibility(View.GONE);
            stock_normal.setVisibility(View.VISIBLE);
            isStock = "false";
        }
        if (isPayment.equals("true")) {
            payment_cheked.setVisibility(View.VISIBLE);
            payment_normal.setVisibility(View.GONE);
            isPayment = "true";
        } else if (isPayment.equals("false")) {
            payment_cheked.setVisibility(View.GONE);
            payment_normal.setVisibility(View.VISIBLE);
            isPayment = "false";
        }
        if (isHarvest != null) {
            if (isHarvest.equals("true")) {
                harvest_cheked.setVisibility(View.VISIBLE);
                harvest_normal.setVisibility(View.GONE);
                isHarvest = "true";
            } else if (isHarvest.equals("false")) {
                harvest_cheked.setVisibility(View.GONE);
                harvest_normal.setVisibility(View.VISIBLE);
                isHarvest = "false";
            }
        }else {
            isHarvest="false";
        }
        if (isDeliver.equals("true")) {
            deliver_cheked.setVisibility(View.VISIBLE);
            deliver_normal.setVisibility(View.GONE);
            isDeliver = "true";
        } else if (isDeliver.equals("false")) {
            deliver_cheked.setVisibility(View.GONE);
            deliver_normal.setVisibility(View.VISIBLE);
            isDeliver = "false";
        }
        neworder_distribution_tv.setText((String) map.get("proudctDistribution"));
        neworder_address_tv.setText((String) map.get("proudctAddress"));
        neworder_remark_tv.setText((String) map.get("proudctRemark"));
        neworder_totalprice.setText((String) map.get("proudctTotalprice"));
        neworder_totalprofit.setText((String) map.get("proudctTotalprofit"));
        neworder_ExpressNumber_val.setText((String) map.get("proudctExpressNumber"));
    }

    // 点击监听事件
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == order_title_leftIv) {
                OrderDetail.this.finish();
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
            } else if (v == neworder_harvest) {
                if ("false".equals(isHarvest)) {
                    harvest_cheked.setVisibility(View.VISIBLE);
                    harvest_normal.setVisibility(View.GONE);
                    isHarvest = "true";
                } else if ("true".equals(isHarvest)) {
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
            } else if (v == neworder_distribution) {
                picker_rel.setVisibility(View.VISIBLE);
            } else if (v == bt_yes) {
                picker_rel.setVisibility(View.GONE);
                neworder_distribution_tv.setText(selectName);
            } else if (v == bt_no) {
                picker_rel.setVisibility(View.GONE);
            } else if (v == neworder_address_tv) {
//                startActivity(new Intent().setClass(OrderDetail.this, CheckAddress.class));
                Intent intent = new Intent();
                intent.putExtra("key", "detail");
                intent.putExtra("clientName", neworder_clientName.getText().toString());
                intent.setClass(OrderDetail.this, CheckAddress.class);
                startActivity(intent);
            }else if (v == neworder_address_iv) {
//                startActivity(new Intent().setClass(OrderDetail.this, CheckAddress.class));
                Intent intent = new Intent();
                intent.putExtra("key", "detail");
                intent.putExtra("clientName", neworder_clientName.getText().toString());
                intent.setClass(OrderDetail.this, CheckAddress.class);
                startActivity(intent);
            }else if (v == neworder_address_tv_copy) {//复制收货地址
                ClipboardManager cm = (ClipboardManager) OrderDetail.this.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(neworder_address_tv.getText().toString());
                Toast.makeText(OrderDetail.this, "复制成功!", Toast.LENGTH_LONG).show();
            } else if (v == neworder_remark) {
//                startActivity(new Intent().setClass(OrderDetail.this, EditRemark.class));
                Intent intent = new Intent();
                intent.putExtra("key", "detail");
                intent.setClass(OrderDetail.this, EditRemark.class);
                startActivity(intent);
            } else if (v == neworder_point) {
//                startActivity(new Intent().setClass(OrderDetail.this, EditRemark.class));
                Intent intent = new Intent();
                intent.putExtra("key", "detail");
                intent.setClass(OrderDetail.this, CheckPoint.class);
                startActivity(intent);
            } else if (v == neworder_delete) {
                DialogUtils.showConfirmDialog(OrderDetail.this, "确认要删除该订单吗", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", "取消");
            } else if (v == neworder_save) {
                save();
            }

        }
    };

    // 滚动选择器选中事件
    PickerScrollView.onSelectListener pickerListener = new PickerScrollView.onSelectListener() {

        @Override
        public void onSelect(Pickers pickers) {
            selectName = pickers.getShowConetnt();
        }
    };

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
        delete();
        map = new HashMap();
        String clientName = neworder_clientName.getText().toString();
        String proudctName = neworder_productName.getText().toString();
        String proudctPoint = neworder_point_tv.getText().toString();
        String proudctClassify = midClassify;
        String proudctCurrency = midCurrency;
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
        map.put("productId", beginProductId);
        map.put("creatTime", creatTime);
        map.put("clientName", clientName);
        map.put("proudctName", proudctName);
        map.put("proudctPoint", proudctPoint);
        map.put("proudctClassify", proudctClassify);
        map.put("proudctCurrency", proudctCurrency);
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

        initDB();

        productList = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String midStr = sharedPreferences.getString("orderList", "camel");
        List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
        }.getType());
        productList.clear();
        productList.addAll(midList);
        productList.add(map);
        String productStr = gson.toJson(productList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orderList", productStr);
        editor.commit();
        Log.e("mly_orderStr", "------------" + productStr);
        Toast.makeText(OrderDetail.this, "修改订单成功", Toast.LENGTH_LONG).show();
        OrderDetail.this.finish();
    }

    private void delete() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String midStr = sharedPreferences.getString("orderList", "camel");
        List<Map> midList = gson.fromJson(midStr, new TypeToken<List<Map>>() {
        }.getType());
        for (int i = 0; i < midList.size(); i++) {
            Map allmap = midList.get(i);
            if (map.get("productId") != null && map.get("productId") != "") {
                if (map.get("productId").equals(allmap.get("productId"))) {
                    midList.remove(i);
                }
            } else if (map.get("clientName").equals(neworder_clientName.getText().toString()) &&
                    map.get("proudctName").equals(neworder_productName.getText().toString())) {
                midList.remove(i);
//                Toast.makeText(OrderDetail.this, "删除成功", Toast.LENGTH_LONG).show();
            }
        }
        String productStr = gson.toJson(midList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orderList", productStr);
        editor.commit();
        deleteDB();
        OrderDetail.this.finish();
    }

    private void tohandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case 3:
                        neworder_address_tv.setText((String) msg.obj);
                        break;
                    case 4:
                        neworder_remark_tv.setText((String) msg.obj);
                        break;
                    case 6:
                        neworder_point_tv.setText((String) msg.obj);
                        break;
                }
            }
        };
    }

    private void initDB() {
        OrderDBHelperUtils orderDBHelperUtils =
                new OrderDBHelperUtils(OrderDetail.this, "order_table", null, 1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = orderDBHelperUtils.getReadableDatabase();

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
        cv.put("proudctRemark", (String) map.get("proudctRemark"));
        cv.put("proudctTotalprice", (String) map.get("proudctTotalprice"));
        cv.put("proudctTotalprofit", (String) map.get("proudctTotalprofit"));
        cv.put("proudctExpressNumber", (String) map.get("proudctExpressNumber"));
        //调用insert方法，将数据插入数据库
        db.insert("order_table", null, cv);
        //关闭数据库
        db.close();
    }

    private void upDataDB() {
        OrderDBHelperUtils orderDBHelperUtils =
                new OrderDBHelperUtils(OrderDetail.this, "order_table", null, 1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = orderDBHelperUtils.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("proudctPoint", "随便");
        //where 子句 "?"是占位符号，对应后面的"1",
        String whereClause = "productId=?";
        //        String [] whereArgs = {String.valueOf(1)};
        String[] whereArgs = {(String) map.get("productId")};
        //参数1 是要更新的表名
        //参数2 是一个ContentValeus对象
        //参数3 是where子句
        db.update("stu_table", cv, whereClause, whereArgs);
        //关闭数据库
        db.close();
    }

    private void deleteDB() {
        OrderDBHelperUtils orderDBHelperUtils =
                new OrderDBHelperUtils(OrderDetail.this, "order_table", null, 1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = orderDBHelperUtils.getReadableDatabase();
        //where 子句 "?"是占位符号，对应后面的"1",
        String whereClause = "productId=?";
        //        String [] whereArgs = {String.valueOf(1)};
        String[] whereArgs = {(String) map.get("productId")};

        db.delete("order_table", whereClause, whereArgs);
        //关闭数据库
        db.close();
    }

    private void searchDB() {
        OrderDBHelperUtils orderDBHelperUtils =
                new OrderDBHelperUtils(OrderDetail.this, "order_table", null, 1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = orderDBHelperUtils.getReadableDatabase();
        Cursor cursor = db.query("order_table", new String[]{"id", "productId", "creatTime", "clientName"
                , "proudctName", "proudctClassify", "proudctCurrency", "proudctPoint", "proudctImageUrl", "proudctInPrice"
                , "proudctCostPrice", "proudctOutPrice", "proudctCount", "proudctIsReceived", "proudctIsStock", "proudctIsPayment"
                , "proudctIsDeliver", "proudctDistribution", "proudctAddress", "proudctRemark", "proudctTotalprice"
                , "proudctTotalprofit", "proudctExpressNumber"
        }, "id=?", new String[]{"1"}, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("sname"));
            String age = cursor.getString(cursor.getColumnIndex("sage"));
            String sex = cursor.getString(cursor.getColumnIndex("ssex"));

        }
        //关闭数据库
        db.close();
    }
}
