package com.yfy.app.tea_event.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.final_tag.TagFinal;

import java.util.List;

public class MyAdapter extends BaseAdapter implements SpinnerAdapter {



    private List<TeaDe> mList;

    private Activity mContext;

//    public void setmList(List<TeaDe> mList) {
//        this.mList = mList;
//        notifyDataSetChanged();
//    }



    public MyAdapter(Activity mContext, List<TeaDe> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        return  -1;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    private String initData(){
        for (TeaDe de:mList){
            if (de.getIscheck().equals(TagFinal.TRUE))
                return de.getTitle();
        }
        return "点击选择";
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(mContext).inflate(R.layout.getview, null);
        TextView tvgetView=(TextView) convertView.findViewById(R.id.tvgetView);


        tvgetView.setText(initData());
        return convertView;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(mContext).inflate(R.layout.getdropdowview, null);
        TextView tvdropdowview=(TextView) convertView.findViewById(R.id.tvgetdropdownview);
        tvdropdowview.setText(mList.get(position).getTitle());
        return convertView;
    }
}
