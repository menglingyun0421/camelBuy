package com.lingyun.camelprocurementservice.orderfragment.aboutclient;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凌云 on 2018/7/25.
 */

public class ClientNameAdapter extends BaseAdapter {
    List<String> list=new ArrayList<>();
    Context context;

    public ClientNameAdapter(Context context,List<String > list) {
        this.list=list;
        this.context=context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_new_client,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        final String name=list.get(position);
        viewHolder.tv.setText(name);
//        viewHolder.item_clientname_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                // 将文本内容放到系统剪贴板里。
//                cm.setText(name);
//                Toast.makeText(context, "复制成功!", Toast.LENGTH_LONG).show();
//            }
//        });
        return convertView;
    }

    class ViewHolder{
        TextView tv;

        public ViewHolder(View view){
            tv=view.findViewById(R.id.item_clientname_tv);
        }
    }
}
