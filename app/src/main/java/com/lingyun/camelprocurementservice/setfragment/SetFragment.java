package com.lingyun.camelprocurementservice.setfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.utils.Aes;
import com.lingyun.camelprocurementservice.utils.DialogUtils;
import com.lingyun.camelprocurementservice.utils.ExcelUtils;
import com.lingyun.camelprocurementservice.utils.FileUtils;
import com.lingyun.camelprocurementservice.utils.OrderDBHelperUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/24.
 */

public class SetFragment extends BaseFragment {
    private Button set_save_btn, set_backup_btn, set_query_btn,set_addpoint_btn,set_excel_btn;
    private String backupsData;
    private String pathName = "camel.txt";
    private String address;
    private List<String> list;
    private TextView set_money_tv;
    private String money;
    private String a[];
    private String[] title = { "商品ID","创建日期","用户名","商品名","币种","采购点","商品分类","商品图片地址","商品成本"
            ,"其它成本","商品售价","商品购买数量","是否收款","是否备货","是否付款","是否发货","是否收货","递送方式"
            ,"快递地址","快递单号","订单总额","预估利润","备注"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        set_save_btn = view.findViewById(R.id.set_save_btn);
        set_backup_btn = view.findViewById(R.id.set_backup_btn);
        set_query_btn = view.findViewById(R.id.set_query_btn);
        set_money_tv = view.findViewById(R.id.set_money_tv);
        set_addpoint_btn = view.findViewById(R.id.set_addpoint_btn);
        set_excel_btn = view.findViewById(R.id.set_excel_btn);


        set_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showConfirmDialog(getActivity(), "确认要备份数据吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveData(backupsData, pathName);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", "取消");

            }
        });

        set_backup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showConfirmDialog(getActivity(), "确认要恢复备份数据吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(pathName);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确认", "取消");
            }
        });

        set_query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryData();
            }
        });

        set_addpoint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SetAddPoint.class));
            }
        });

        set_excel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDB ();
            }
        });
    }

    private void initData() {
        Gson gson = new Gson();
        StringBuffer buffer = new StringBuffer();

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("clientName", Context.MODE_PRIVATE);
        String username = sharedPreferences2.getString("username", "camel");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
        String productList = sharedPreferences3.getString("productList", "camel");

        SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String orderList = sharedPreferences4.getString("orderList", "camel");

        SharedPreferences sharedPreferences5 = getActivity().getSharedPreferences("GoodPoint", Context.MODE_PRIVATE);
        String pointList = sharedPreferences5.getString("point", "camel");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
        if (!username.equals("camel")) {
            list = gson.fromJson(username, new TypeToken<List<String>>() {
            }.getType());
            buffer.append(list.size() + "##");
            for (int i = 0; i < list.size(); i++) {
                address = sharedPreferences.getString(list.get(i), "camel");
                buffer.append(address + "##");
            }
        } else {
            buffer.append("1" + "##");
            buffer.append("camel" + "##");
        }

        buffer.append(username + "##");
        buffer.append(productList + "##");
        buffer.append(orderList + "##");
        buffer.append(pointList);
        backupsData = buffer.toString();

        //点数相关
        String moneyPiont= FileUtils.getData("/Android/data/com.systemcamel.point","camel.txt");
        String masterPassword = "tebiefuza";
        String decryptingCode = null;
        try {
            decryptingCode = Aes.decrypt(masterPassword, moneyPiont);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("aes_mly","---------------------"+e);
        }
        set_money_tv.setText(decryptingCode);

    }

    private void saveData(String data, String pathName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
            File saveFile = new File(sdCardDir, pathName);

            //写数据
            try {
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(data.getBytes());
                fos.close();
                Toast.makeText(getActivity(), "保存数据成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "保存数据失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getData(String pathName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
            File saveFile = new File(sdCardDir, pathName);

            //读数据
            try {
                FileInputStream fis = new FileInputStream(saveFile);
                int len = 0;
                int size=fis.available();
                byte[] buf = new byte[size];
                StringBuffer sb = new StringBuffer();
                while ((len = fis.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                a = sb.toString().split("##");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "恢复数据失败", Toast.LENGTH_LONG).show();
            }

            String count=a[0];
            int clientCount=Integer.valueOf(a[0]).intValue();
//            int clientCount=10;
//            int clientCount=Integer.parseInt(count);
//            BigInteger clientCount = new BigInteger(count);

            Gson gson = new Gson();
            List<String> list = gson.fromJson(a[clientCount+1], new TypeToken<List<String>>() {
            }.getType());
            String clientAddress;
            SharedPreferences sharedPreferences5 = getActivity().getSharedPreferences("clientAddress", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences5.edit();
            if ("camel".equals(a[1])){
//                return;
            }else {
                for (int i = 0; i < list.size(); i++) {
                    clientAddress = a[i+1];
                    editor.putString(list.get(i), clientAddress);
                    editor.commit();
                }
            }

            SharedPreferences sharedPreferences6 = getActivity().getSharedPreferences("clientName", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences6.edit();
            editor2.putString("username", a[clientCount+1]);
            editor2.commit();

            SharedPreferences sharedPreferences7 = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor3 = sharedPreferences7.edit();
            editor3.putString("productList", a[clientCount+2]);
            editor3.commit();

            SharedPreferences sharedPreferences8 = getActivity().getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor4 = sharedPreferences8.edit();
            editor4.putString("orderList", a[clientCount+3]);
            editor4.commit();

            if (a.length==(clientCount+5)){
                SharedPreferences sharedPreferences9 = getActivity().getSharedPreferences("GoodPoint", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor5 = sharedPreferences9.edit();
                editor5.putString("point", a[clientCount+4]);
                editor5.commit();
            }


            Toast.makeText(getActivity(), "恢复数据成功", Toast.LENGTH_LONG).show();


        }
    }

    private void queryData() {
        SharedPreferences sharedPreferences9 = getActivity().getSharedPreferences("clientName", Context.MODE_PRIVATE);
        String username = sharedPreferences9.getString("username", "camel");
        Toast.makeText(getActivity(), username, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    //后续增加的用数据库的方式遍历功能
    private void initDB (){
        OrderDBHelperUtils orderDBHelperUtils=
                new OrderDBHelperUtils(getActivity(),"order_table",null,1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db =orderDBHelperUtils.getReadableDatabase();

        String sql = "select * from order_table";
        Cursor cursor2 = db.rawQuery(sql, null);

        ArrayList <ArrayList> orderlist=new ArrayList<>();
        while (cursor2.moveToNext()) {
            ArrayList list=new ArrayList();
            list.add(cursor2.getString(1));
            list.add(cursor2.getString(2));
            list.add(cursor2.getString(3));
            list.add(cursor2.getString(4));
            list.add(cursor2.getString(5));
            list.add(cursor2.getString(6));
            list.add(cursor2.getString(7));
            list.add(cursor2.getString(8));
            list.add(cursor2.getString(9));
            list.add(cursor2.getString(10));
            list.add(cursor2.getString(11));
            list.add(cursor2.getString(12));
            list.add(cursor2.getString(13));
            list.add(cursor2.getString(14));
            list.add(cursor2.getString(15));
            list.add(cursor2.getString(16));
            list.add(cursor2.getString(17));
            list.add(cursor2.getString(18));
            list.add(cursor2.getString(19));
            list.add(cursor2.getString(20));
            list.add(cursor2.getString(21));
            list.add(cursor2.getString(22));
            orderlist.add(list);
//            String name=cursor2.getString(cursor2.getColumnIndex("productId"));
//            Log.e("mly_orderStrDB", "------------" + name);
        }
        //关闭数据库
        db.close();

        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录  "/sdcard"
        ExcelUtils.initExcel(sdCardDir.toString() + "/camelOrder.xls", title);
        ExcelUtils.writeObjListToExcel(orderlist, sdCardDir + "/camelOrder.xls", getActivity());
    }
}

