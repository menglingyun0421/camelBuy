package com.lingyun.camelprocurementservice.orderfragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingyun.camelprocurementservice.R;
import com.lingyun.camelprocurementservice.orderfragment.neworder.NewOrderActivity;

import java.util.List;
import java.util.Map;

import static com.lingyun.camelprocurementservice.R.color.colorGray;

/**
 * Created by 凌云 on 2018/7/31.
 */

public class OrderAllAdapter extends BaseAdapter{
    private Context context;

    public OrderAllAdapter(Context context, List<Map> list) {
        this.context = context;
        this.list = list;
    }

    private List<Map> list;



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_show_order,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Map map=list.get(position);

        Glide.with(context).load((String) map.get("proudctImageUrl")).centerCrop().thumbnail(0.3f).into(viewHolder.order_item_iv);
        viewHolder.newproduct_item_name_tv.setText((String) map.get("clientName"));
        viewHolder.order_item_time.setText((String) map.get("creatTime"));
        viewHolder.newproduct_item_productName_tv.setText((String) map.get("proudctName"));
        viewHolder.order_item_productCount.setText((String) map.get("proudctCount"));
        viewHolder.newproduct_item_totalpriceVal.setText((String) map.get("proudctTotalprice"));
        viewHolder.newproduct_item_totalProfitVal.setText((String) map.get("proudctTotalprofit"));
        if (((String) map.get("proudctIsReceived")).equals("false")){
            viewHolder.newproduct_item_isReceived.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }else {
            viewHolder.newproduct_item_isReceived.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        if (((String) map.get("proudctIsStock")).equals("false")){
            viewHolder.newproduct_item_isStock.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }else {
            viewHolder.newproduct_item_isStock.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        if (((String) map.get("proudctIsPayment")).equals("false")){
            viewHolder.newproduct_item_isPayment.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }else {
            viewHolder.newproduct_item_isPayment.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        if (((String) map.get("proudctIsDeliver")).equals("false")){
            viewHolder.newproduct_item_isDeliver.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }else {
            viewHolder.newproduct_item_isDeliver.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        if (("true").equals((String) map.get("proudctIsHarvest"))){
            viewHolder.newproduct_item_isHarvest.setBackgroundColor(Color.parseColor("#00ff00"));
        }else {
            viewHolder.newproduct_item_isHarvest.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

        return convertView;
    }

    class ViewHolder{
        ImageView order_item_iv;
        TextView newproduct_item_name_tv,order_item_time,newproduct_item_productName_tv,order_item_productCount;
        TextView newproduct_item_totalpriceVal,newproduct_item_totalProfitVal;
        TextView newproduct_item_isReceived,newproduct_item_isStock,newproduct_item_isPayment,newproduct_item_isDeliver,newproduct_item_isHarvest;

        public  ViewHolder (View view){
            order_item_iv=view.findViewById(R.id.order_item_iv);
            newproduct_item_name_tv=view.findViewById(R.id.newproduct_item_name_tv);
            order_item_time=view.findViewById(R.id.order_item_time);
            newproduct_item_productName_tv=view.findViewById(R.id.newproduct_item_productName_tv);
            order_item_productCount=view.findViewById(R.id.order_item_productCount);
            newproduct_item_totalpriceVal=view.findViewById(R.id.newproduct_item_totalpriceVal);
            newproduct_item_totalProfitVal=view.findViewById(R.id.newproduct_item_totalProfitVal);
            newproduct_item_isReceived=view.findViewById(R.id.newproduct_item_isReceived);
            newproduct_item_isStock=view.findViewById(R.id.newproduct_item_isStock);
            newproduct_item_isPayment=view.findViewById(R.id.newproduct_item_isPayment);
            newproduct_item_isDeliver=view.findViewById(R.id.newproduct_item_isDeliver);
            newproduct_item_isHarvest=view.findViewById(R.id.newproduct_item_isHarvest);

        }
    }
}
