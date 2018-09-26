package com.lingyun.camelprocurementservice.utils;

import java.util.Calendar;

/**
 * Created by 凌云 on 2018/8/13.
 */

public class CountDaysUtils {

    Calendar cal   =   Calendar.getInstance();//可以对每个时间域单独修改

    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);

    //计算当前月有多少天
    public static int countDaysOfMonth(int month,int year){
        if (year%4==0){
            if (month==1||month==3||month==5||month==7 ||month==8||month==10||month==12){
                return 31;
            }else if (month==4||month==6||month==9||month==11 ){
                return 30;
            }else {
                return 29;
            }
        }else {
            if (month==1||month==3||month==5||month==7 ||month==8||month==10||month==12){
                return 31;
            }else if (month==4||month==6||month==9||month==11 ){
                return 30;
            }else {
                return 28;
            }
        }
    }

    //根据上一个月计算前一个月
    public static int yesterMonth(int month){
        if (month-1==0){
            return 12;
        }else {
            return month-1;
        }
    }

    //根据上一个月计算前一年
    public static int yesterYear(int month,int year){
        if (month-1==0){
            return year-1;
        }else {
            return year;
        }
    }
}
