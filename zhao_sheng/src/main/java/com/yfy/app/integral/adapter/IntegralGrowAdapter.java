package com.yfy.app.integral.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.beans.IntegralGrow;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/6.
 */

public class IntegralGrowAdapter extends XlistAdapter<IntegralGrow> {


    String no="未填写";

    public IntegralGrowAdapter(Context context, List<IntegralGrow> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<IntegralGrow> list) {
        IntegralGrow grow=list.get(position);
        holder.getView(TextView.class,R.id.integral_grow_item_title).setText(grow.getTagname());
        TextView content= holder.getView(TextView.class,R.id.integral_grow_item_name);
        ImageView set=holder.getView(ImageView.class,R.id.integral_grow_item_seticon);
        String name;
        if (StringJudge.isEmpty(grow.getResult())){
            content.setTextColor(Color.GRAY);
            name=no;
        }else{
            name=grow.getResult();
            content.setTextColor(Color.BLACK);
        }
        if (grow.getIsedit().equals("true")){
            set.setVisibility(View.VISIBLE);
        }else{
            set.setVisibility(View.INVISIBLE);
        }

        content.setText(name);

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo res=new ResInfo();
        res.layout= R.layout.integral_grow_item;
        res.initIds=new int[]{
                R.id.integral_grow_item_name,
                R.id.integral_grow_item_title,
                R.id.integral_grow_item_seticon,
        };
        return res;
    }
}
