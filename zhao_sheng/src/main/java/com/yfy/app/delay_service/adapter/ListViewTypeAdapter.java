package com.yfy.app.delay_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.bean.OperType;
import com.yfy.final_tag.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListViewTypeAdapter extends BaseAdapter {


    private Context context;
    private List<OperType> datas;
//    private Handler                 handler;
    private int checked = -1;//初始选择为-1，position没有-1嘛，那就是谁都不选咯


    public void setDatas(List<OperType> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public ListViewTypeAdapter(Context context) {
        this.datas = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.public_checked , null);
            viewHolder.name = convertView.findViewById(android.R.id.text1);
//            viewHolder.select = (RadioButton)convertView.findViewById(R.id.id_select);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setChecked(datas.get(position).isIselect());
        viewHolder.name.setText(StringUtils.getTextJoint("%1$s",datas.get(position).getOpearname()));
        return convertView;
    }

    public class ViewHolder{
        CheckedTextView name;

    }

}
