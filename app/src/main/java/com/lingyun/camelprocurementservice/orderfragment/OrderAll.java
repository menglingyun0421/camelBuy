package com.lingyun.camelprocurementservice.orderfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.orderfragment.aboutproduct.NewProductAdapter;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;
import com.lingyun.camelprocurementservice.utils.OrderDBHelperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/31.
 */

public class OrderAll extends BaseFragment{
    private ListView order_finish_lv;
    private List<Map> orderList=new ArrayList<>();
    private TextView order_unfinish_tv;
    private LinearLayout order_unfinish_ll;
    private OrderAllAdapter adapter;
    private EditText order_unfinish_et;
    private List<Map> searList=new ArrayList<>();
    private List<Map> searchMidList=new ArrayList<>();
    private List<Map> timeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_order_finish, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
        initDB();

        order_finish_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson=new Gson();
                String order=gson.toJson(timeList.get(position));
                Intent intent=new Intent();
                intent.setClass(getActivity(),OrderDetail.class);
                intent.putExtra("order", order );
                startActivity(intent);
            }
        });
    }

    private void initView(){
        order_finish_lv=getActivity().findViewById(R.id.order_finish_lv);
        order_unfinish_tv=getActivity().findViewById(R.id.order_unfinish_tv);
        order_unfinish_ll=getActivity().findViewById(R.id.order_unfinish_ll);
        order_unfinish_et=getActivity().findViewById(R.id.order_unfinish_et);

        order_unfinish_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searList.clear();
                timeList.clear();
                timeList.addAll(searchMidList);
                String serachNnam=order_unfinish_et.getText().toString();
                if (!serachNnam.equals("")){
                    for (int i=0;i<timeList.size();i++){
                        if (timeList.get(i).get("clientName").toString().contains(serachNnam)){
                            searList.add(timeList.get(i));
                        }
                    }
                    timeList.clear();
                    timeList.addAll(searList);
                    adapter.notifyDataSetChanged();
                    order_unfinish_tv.setText(timeList.size()+"");
                }else {
                    if (adapter!=null){
                        adapter.notifyDataSetChanged();
                        order_unfinish_tv.setText(timeList.size()+"");
                    }
                }
            }
        });
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String midList = sharedPreferences.getString("orderList", "camel");
        Gson gson = new Gson();
        if (midList.equals("camel")) {
            order_unfinish_ll.setVisibility(View.GONE);
            return;
        }else {
            orderList = gson.fromJson(midList, new TypeToken<List<Map>>() {
            }.getType());
            order_unfinish_tv.setText(orderList.size()+"");
            Log.e("mly_orderStr", "------------" + orderList);
            timeList=new ArrayList<>();
            for (int i=orderList.size();i>0;i--){
                timeList.add(orderList.get(i-1));
            }
            searchMidList.clear();
            searchMidList.addAll(timeList);
            adapter=new OrderAllAdapter(getActivity(),timeList);
            order_finish_lv.setAdapter(adapter);
        }
//        initDB ();
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
//        initDB();
    }

    //后续增加的用数据库的方式遍历功能
    private void initDB (){
        OrderDBHelperUtils orderDBHelperUtils=
                new OrderDBHelperUtils(getActivity(),"order_table",null,1);

        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db =orderDBHelperUtils.getReadableDatabase();
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
//        Cursor cursor = db.query("order_table", new String[]{"productId","creatTime","clientName"
//                ,"proudctName","proudctClassify","proudctCurrency","proudctPoint","proudctImageUrl","proudctInPrice"
//                ,"proudctCostPrice","proudctOutPrice","proudctCount","proudctIsReceived","proudctIsStock","proudctIsPayment"
//                ,"proudctIsDeliver","proudctDistribution","proudctAddress","proudctRemark","proudctTotalprice"
//                ,"proudctTotalprofit","proudctExpressNumber"
////                }, "productId=?", new String[]{"87125236"}, null, null, null);
//                }, "finished=?", new String[]{"1"}, null, null, null);
//        while(cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex("productId"));
//            String age = cursor.getString(cursor.getColumnIndex("clientName"));
//            String sex = cursor.getString(cursor.getColumnIndex("proudctName"));
//            Log.e("mly_orderStrDB", "------------" + name+age+sex);
//        }

        String sql = "select * from order_table";
        Cursor cursor2 = db.rawQuery(sql, null);
        while (cursor2.moveToNext()) {
            String name=cursor2.getString(cursor2.getColumnIndex("productId"));
            Log.e("mly_orderStrDB", "------------" + name);
        }
        //关闭数据库
        db.close();
    }
}
