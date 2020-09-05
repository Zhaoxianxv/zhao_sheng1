package com.yfy.app.allclass.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.allclass.beans.TopBean;
import com.yfy.base.XlistAdapter;
import com.yfy.base.adapter.AbstractAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by yfyandr on 2018/3/12.
 */

public class ItemAdapter extends BaseAdapter {
    private List<TopBean> list=new ArrayList<>();
    private Context context;

    public ItemAdapter(List<TopBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ViewHolder holder = null;
        TopBean bean=list.get(position);
        if (convertView == null) {
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_person_top_item, parent, false);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.top_image);
            holder.reply = (TextView) convertView.findViewById(R.id.shape_top_reply);
            holder.name = (TextView) convertView.findViewById(R.id.shape_top_name);
            holder.send = (TextView) convertView.findViewById(R.id.shape_top_send);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (position==0){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.name.setTypeface(Typeface.DEFAULT_BOLD);
        }else{
            holder.imageView.setVisibility(View.GONE);
            holder.name.setTypeface(Typeface.MONOSPACE);
        }

        holder.name.setText(bean.getTitle());
        holder.send.setText("发帖："+bean.getPost());
        holder.reply.setText("评论："+bean.getReply());


        return convertView;


    }
    static class ViewHolder{
        TextView name;
        TextView reply;
        TextView send;
        ImageView imageView;
    }
}
