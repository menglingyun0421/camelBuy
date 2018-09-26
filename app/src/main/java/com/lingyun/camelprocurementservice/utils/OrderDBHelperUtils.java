package com.lingyun.camelprocurementservice.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 凌云 on 2018/9/11.
 */

public class OrderDBHelperUtils extends SQLiteOpenHelper {
    public static final int VERSION = 1;

    //必须要有构造函数
    public OrderDBHelperUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 当第一次创建数据库的时候，调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table order_table(_id integer primary key autoincrement,productId text,creatTime text" +
                ",clientName text,proudctName text,proudctClassify text,proudctCurrency text,proudctPoint text,proudctImageUrl text" +
                ",proudctInPrice text,proudctCostPrice text,proudctOutPrice text,proudctCount text,proudctIsReceived text" +
                ",proudctIsStock text,proudctIsPayment text,proudctIsDeliver text,proudctDistribution text,proudctAddress text," +
                "proudctRemark text,proudctTotalprice text,proudctTotalprofit text,proudctExpressNumber text)";
        db.execSQL(sql);
    }

    //当更新数据库的时候执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
