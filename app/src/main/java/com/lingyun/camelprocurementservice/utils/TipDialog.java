package com.lingyun.camelprocurementservice.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lingyun.camelprocurementservice.R;


public class TipDialog extends Dialog {

	Activity activity;
	private View.OnClickListener okClickListener;
//	private View.OnClickListener cancelClickListener;
	private String msg;
	private TextView txtMsg;
	private Button btnOk;
//	private Button btnCancel;
	private String btnOkText;
	private String btnCancelText;
	public TipDialog(Activity activity) {
		super(activity, R.style.AppTheme);
		this.activity = activity;
	}
	public TipDialog(Activity activity, String btnOkText) {
		super(activity, R.style.MyDialog);
		this.activity = activity;
		this.btnOkText = btnOkText;
	}
	public void setMessage(String msg){
		this.msg = msg;
	}
	public void setOkClickListener(View.OnClickListener okClickListener){
		this.okClickListener = okClickListener;
	}
//	public void setCancelListener(View.OnClickListener cancelClickListener){
//		this.cancelClickListener = cancelClickListener;
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub_tip_dialog);
		setCanceledOnTouchOutside(false);//单击dialog之外的地方，不可以dismiss掉dialog
		txtMsg = (TextView) findViewById(R.id.txt_msg);
		btnOk = (Button) findViewById(R.id.btnOk);
//		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtMsg.setText(this.msg);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TipDialog.this.dismiss();
				if(okClickListener!=null)
					okClickListener.onClick(btnOk);
			}
		});
//		btnCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				TipDialog.this.dismiss();
//				if(cancelClickListener!=null)
//					cancelClickListener.onClick(btnCancel);
//			}
//		});
		
		if(null!=btnOkText && !"".equals(btnOkText)){
			btnOk.setText(btnOkText);
		}
//		if(null!=btnCancelText && !"".equals(btnCancelText)){
//			btnCancel.setText(btnCancelText);
//		}
		
	}
	public String getBtnOkText() {
		return btnOkText;
	}
	public void setBtnOkText(String btnOkText) {
		this.btnOkText = btnOkText;
	}
//	public String getBtnCancelText() {
//		return btnCancelText;
//	}
//	public void setBtnCancelText(String btnCancelText) {
//		this.btnCancelText = btnCancelText;
//	}

}
