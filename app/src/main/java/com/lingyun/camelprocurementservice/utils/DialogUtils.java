package com.lingyun.camelprocurementservice.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by 凌云 on 2018/8/7.
 */

public class DialogUtils {
    //确认按钮的实现，在调用时可以添加确定或取消
    public static void showConfirmDialog(Activity activity, String msg, View.OnClickListener okListener, View.OnClickListener cancelListener, String btnOk, String btnCancel){
        ConfirmInfoDialog dialog = new ConfirmInfoDialog(activity,btnOk,btnCancel);
        dialog.setMessage(msg);
        dialog.setOkClickListener(okListener);
        dialog.setCancelListener(cancelListener);
        dialog.show();
    }

    //确认按钮的实现
    public static void TipDialog(Activity activity, String msg, View.OnClickListener okListener, String btnOk){
        TipDialog dialog = new TipDialog(activity,btnOk);
        dialog.setMessage(msg);
        dialog.setOkClickListener(okListener);
        dialog.show();
    }

}
