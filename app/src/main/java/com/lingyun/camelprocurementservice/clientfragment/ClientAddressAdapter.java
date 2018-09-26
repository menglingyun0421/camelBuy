package com.lingyun.camelprocurementservice.clientfragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lingyun.camelprocurementservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 凌云 on 2018/7/25.
 */

public class ClientAddressAdapter extends BaseAdapter {
    //定义成员变量mTouchItemPosition,用来记录手指触摸的EditText的位置
    private int mTouchItemPosition = -1;
    List<String> list = new ArrayList<>();
    Context context;

    public ClientAddressAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_edit_address, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                mTouchItemPosition = (Integer) v.getTag();
                return false;
            }

        });

        // 让ViewHolder持有一个TextWathcer，动态更新position来防治数据错乱；不能将position定义成final直接使用，必须动态更新
        viewHolder.mTextWatcher = new MyTextWatcher();
        viewHolder.et.addTextChangedListener(viewHolder.mTextWatcher);
        viewHolder.updatePosition(position);

        final String name = (String) list.get(position);
        viewHolder.et.setText(name);

        viewHolder.item_clientname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(name);
                Toast.makeText(context, "复制成功!", Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.et.setTag(position);//设置标记

        //处理焦点问题
        if (mTouchItemPosition == position) {
            viewHolder.et.requestFocus();
            viewHolder.et.setSelection(viewHolder.et.getText().length());
        } else {
            viewHolder.et.clearFocus();
        }

        if (position == (list.size() - 1)) {
            Message message = new Message();
            message.what = 1;
            message.obj = list;
            ClientDetail.handler.sendMessage(message);
        }
        return convertView;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动 false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    class ViewHolder {
        EditText et;
        MyTextWatcher mTextWatcher;
        Button item_clientname_btn;

        public ViewHolder(View view) {
            et = view.findViewById(R.id.item_clientname_et);
            item_clientname_btn = view.findViewById(R.id.item_clientname_btn);
        }

        //动态更新TextWathcer的position
        public void updatePosition(int position) {
            mTextWatcher.updatePosition(position);
        }
    }

    class MyTextWatcher implements TextWatcher {
        //由于TextWatcher的afterTextChanged中拿不到对应的position值，所以自己创建一个子类
        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            list.set(mPosition, s.toString());
        }
    }
}
