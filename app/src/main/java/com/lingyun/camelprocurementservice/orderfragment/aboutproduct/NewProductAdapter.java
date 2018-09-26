package com.lingyun.camelprocurementservice.orderfragment.aboutproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingyun.camelprocurementservice.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/27.
 */

public class NewProductAdapter extends BaseAdapter {
    private Context context;
    private List<Map> list;

    public NewProductAdapter(Context context, List<Map> list) {
        this.context = context;
        this.list = list;
    }

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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_new_product,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Map map=list.get(position);
        viewHolder.newproduct_item_name_tv.setText((String) map.get("proudctName"));
        viewHolder.newproduct_item_priceVal_tv.setText((String) map.get("proudctInPrice")+"+"+(String) map.get("proudctCostPrice"));
        viewHolder.newproduct_item_profiVal_tv.setText((String) map.get("proudctProfiPrice"));
        viewHolder.newproduct_item_outprice_tv.setText((String) map.get("proudctOutPrice"));
        String imageUrl= (String) map.get("proudctImageUrl");
        if(imageUrl!=null){
            Glide.with(context).load(imageUrl).centerCrop().thumbnail(0.3f).into(viewHolder.newproduct_item_iv);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView newproduct_item_iv;
        TextView newproduct_item_name_tv,newproduct_item_priceVal_tv,newproduct_item_profiVal_tv,newproduct_item_outprice_tv;
        public ViewHolder(View view){
            newproduct_item_iv=view.findViewById(R.id.newproduct_item_iv);
            newproduct_item_name_tv=view.findViewById(R.id.newproduct_item_name_tv);
            newproduct_item_priceVal_tv=view.findViewById(R.id.newproduct_item_priceVal_tv);
            newproduct_item_profiVal_tv=view.findViewById(R.id.newproduct_item_profiVal_tv);
            newproduct_item_outprice_tv=view.findViewById(R.id.newproduct_item_outprice_tv);
        }
    }
}
