package com.lingyun.camelprocurementservice.statisticsfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.basepage.BaseFragment;
import com.lingyun.camelprocurementservice.view.HistogramView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/24.
 */

public class StatisticsFragment extends BaseFragment {
    private HistogramView green;
    private HistogramView green2;
    private List<Map> orderList = new ArrayList<>();
    private List<Long> timeList=new ArrayList<>();
    private List<String> monthNode=new ArrayList<>();
    private List<String> weekNode=new ArrayList<>();
    private float monthMoney6=0.0f,monthMoney5=0.0f,monthMoney4=0.0f,monthMoney3=0.0f,monthMoney2=0.0f,monthMoney1=0.0f;
    private float weekMoney6=0.0f,weekMoney5=0.0f,weekMoney4=0.0f,weekMoney3=0.0f,weekMoney2=0.0f,weekMoney1=0.0f;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        green = (HistogramView) view.findViewById(R.id.green);
        green2 = (HistogramView) view.findViewById(R.id.green2);

        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (monthNode!=null){
            String[] ySteps = new String[]{"10k", "7.5k", "5k", "2.5k", "0"};
            String[] xWeeks1 = new String[]{"01-05", "06-10", "11-15", "15-20", "21-25", "25-00"};
            String[] xWeeks2 = new String[]{monthNode.get(0).substring(5,7), monthNode.get(1).substring(5,7), monthNode.get(2).substring(5,7),
                    monthNode.get(3).substring(5,7), monthNode.get(4).substring(5,7), monthNode.get(5).substring(5,7)};
            int[] progress1 = {(int)weekMoney1, (int)weekMoney2, (int)weekMoney3, (int)weekMoney4, (int)weekMoney5, (int)weekMoney6};
            int[] progress2 = {(int)monthMoney1, (int)monthMoney2, (int)monthMoney3, (int)monthMoney4, (int)monthMoney5, (int)monthMoney6};
            int[] text = {0, 0, 0, 0, 0, 0,};
            int[] aniProgress = new int[]{0, 0, 0, 0, 0, 0, };

            green.setData(ySteps, xWeeks1, progress1, text, aniProgress);
            green.start(2);//flag:1不使用动画，2使用动画
            green2.setData(ySteps, xWeeks2, progress2, text, aniProgress);
            green2.start(2);//flag:1不使用动画，2使用动画
        }
        return view;
    }

    private void initData() throws ParseException {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProductOrder", Context.MODE_PRIVATE);
        String orderStr = sharedPreferences.getString("orderList", "camel");
        if ("camel".equals(orderStr)) {
            getTimeNode();
            return;
        } else {
            getTimeNode();//获取各个时间节点
            String formatStr="yyyy-MM-dd";
            List<Long> weekLongNode=new ArrayList<>();
            List<Long> MonthLongNode=new ArrayList<>();

            //将时间节点转换为long行数据
            for (int i=0;i<7;i++){
//                Log.e("timeList_mly","----------------"+weekNode.get(i));
                weekLongNode.add(stringToLong(weekNode.get(i),formatStr));
                MonthLongNode.add(stringToLong(monthNode.get(i),formatStr));
//                Log.e("timeList_mly","----------------"+stringToLong(weekNode.get(i),formatStr));
            }

            Gson gson = new Gson();
            orderList = gson.fromJson(orderStr, new TypeToken<List<Map>>() {
            }.getType());

            //循环每一个订单，将订单利润添加到对应的数值上去
            for (int i = 0; i < orderList.size(); i++) {
                String time = (String) orderList.get(i).get("creatTime");
                //最近一个月前5天
                if (stringToLong(time,formatStr)>=weekLongNode.get(0)&&stringToLong(time,formatStr)<weekLongNode.get(1)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney1+=profitPrice;
                }
                //最近一个月5-10
                else if (stringToLong(time,formatStr)>=weekLongNode.get(1)&&stringToLong(time,formatStr)<weekLongNode.get(2)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney2+=profitPrice;
                }
                //最近一个月10-15
                else if (stringToLong(time,formatStr)>=weekLongNode.get(2)&&stringToLong(time,formatStr)<weekLongNode.get(3)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney3+=profitPrice;
                }
                //最近一个月15-20
                else if (stringToLong(time,formatStr)>=weekLongNode.get(3)&&stringToLong(time,formatStr)<weekLongNode.get(4)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney4+=profitPrice;
                }
                //最近一个月20-25
                else if (stringToLong(time,formatStr)>=weekLongNode.get(4)&&stringToLong(time,formatStr)<weekLongNode.get(5)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney5+=profitPrice;
                }
                //最近一个月25-下月1
                else if (stringToLong(time,formatStr)>=weekLongNode.get(5)&&stringToLong(time,formatStr)<weekLongNode.get(6)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    weekMoney6+=profitPrice;
                }
                //近半年的最后一个月
                else if (stringToLong(time,formatStr)>=MonthLongNode.get(6)&&stringToLong(time,formatStr)<MonthLongNode.get(5)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney6+=profitPrice;
                }

                else if (stringToLong(time,formatStr)>=MonthLongNode.get(5)&&stringToLong(time,formatStr)<MonthLongNode.get(4)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney5+=profitPrice;
                }

                else if (stringToLong(time,formatStr)>=MonthLongNode.get(4)&&stringToLong(time,formatStr)<MonthLongNode.get(3)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney4+=profitPrice;
                }

                else if (stringToLong(time,formatStr)>=MonthLongNode.get(3)&&stringToLong(time,formatStr)<MonthLongNode.get(2)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney3+=profitPrice;
                }

                else if (stringToLong(time,formatStr)>=MonthLongNode.get(2)&&stringToLong(time,formatStr)<MonthLongNode.get(1)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney2+=profitPrice;
                }

                else if (stringToLong(time,formatStr)>=MonthLongNode.get(1)&&stringToLong(time,formatStr)<MonthLongNode.get(0)){
                    String price= (String) orderList.get(i).get("proudctTotalprofit");
                    float profitPrice=Float.parseFloat(price);
                    monthMoney1+=profitPrice;
                }





            }



        }
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public  void getTimeNode(){
        String str1,str2;

        //YEAR,MONTH,DATE,HOUR_OF_DAY,MINUTE,SECOND
        Calendar   cal   =   Calendar.getInstance();//可以对每个时间域单独修改

        //Month1 and Week
        cal.add(Calendar.MONTH,   -0);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth1=str2+"-01";
        monthNode.add(yesterMonth1);
//        Log.e("timeList_mly","----------------"+yesterMonth1);
        String yesterWeek1=yesterMonth1;
        String yesterWeek2=str2+"-06";
        String yesterWeek3=str2+"-11";
        String yesterWeek4=str2+"-16";
        String yesterWeek5=str2+"-21";
        String yesterWeek6=str2+"-26";
        Calendar   cal2   =   Calendar.getInstance();//可以对每个时间域单独修改
        cal2.add(Calendar.MONTH,   +1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal2.getTime());
        str2=str1.substring(0,7);
        String yesterWeek7=str2+"-01";
        weekNode.add(yesterWeek1);
        weekNode.add(yesterWeek2);
        weekNode.add(yesterWeek3);
        weekNode.add(yesterWeek4);
        weekNode.add(yesterWeek5);
        weekNode.add(yesterWeek6);
        weekNode.add(yesterWeek7);
//        Log.e("timeList_mly","----------------"+yesterWeek1);
//        Log.e("timeList_mly","----------------"+yesterWeek2);
//        Log.e("timeList_mly","----------------"+yesterWeek3);
//        Log.e("timeList_mly","----------------"+yesterWeek4);
//        Log.e("timeList_mly","----------------"+yesterWeek5);
//        Log.e("timeList_mly","----------------"+yesterWeek6);
//        Log.e("timeList_mly","----------------"+yesterWeek7);


        //Month2
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth2 = str2+"-01";
        monthNode.add(yesterMonth2);
//        Log.e("timeList_mly","----------------"+yesterMonth2);

        //Month3
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth3 = str2+"-01";
        monthNode.add(yesterMonth3);
//        Log.e("timeList_mly","----------------"+yesterMonth3);

        //Month4
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth4 = str2+"-01";
        monthNode.add(yesterMonth4);
//        Log.e("timeList_mly","----------------"+yesterMonth4);

        //Month5
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth5 = str2+"-01";
        monthNode.add(yesterMonth5);
//        Log.e("timeList_mly","----------------"+yesterMonth5);

        //Month6
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth6 = str2+"-01";
        monthNode.add(yesterMonth6);
//        Log.e("timeList_mly","----------------"+yesterMonth6);

        //Month7
        cal.add(Calendar.MONTH,   -1);
        str1 = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        str2=str1.substring(0,7);
        String yesterMonth7 = str2+"-01";
        monthNode.add(yesterMonth7);
//        Log.e("timeList_mly","----------------"+yesterMonth7);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        monthMoney6=0.0f;monthMoney5=0.0f;monthMoney4=0.0f;monthMoney3=0.0f;monthMoney2=0.0f;monthMoney1=0.0f;
        weekMoney6=0.0f;weekMoney5=0.0f;weekMoney4=0.0f;weekMoney3=0.0f;weekMoney2=0.0f;weekMoney1=0.0f;
    }
}
