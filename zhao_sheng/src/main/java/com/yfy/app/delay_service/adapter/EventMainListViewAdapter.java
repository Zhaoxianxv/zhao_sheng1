package com.yfy.app.delay_service.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.bean.EventGrade;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class EventMainListViewAdapter extends BaseAdapter {


    private Context context;
    private List<EventGrade> datas;
//    private Handler                 handler;
    private int checked = -1;//初始选择为-1，position没有-1嘛，那就是谁都不选咯


    public void setDatas(List<EventGrade> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public EventMainListViewAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.delay_service_left_item , null);
            viewHolder.name = convertView.findViewById(R.id.moral_list_item_content);
//            viewHolder.select = (RadioButton)convertView.findViewById(R.id.id_select);
            viewHolder.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf"));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
//        if (position==0){
//            viewHolder.name.setBackgroundColor(ColorRgbUtil.getWhite());
//        }
        viewHolder.name.setTextColor(ColorRgbUtil.getBaseText());
        viewHolder.name.setTextSize(14f);
        viewHolder.name.setText(StringUtils.getTextJoint("%1$s(%2$d)",datas.get(position).getGradename(),datas.get(position).getElective_list().size()));
        return convertView;
    }

    public class ViewHolder{
        TextView name;
//        RadioButton select;
    }

}
