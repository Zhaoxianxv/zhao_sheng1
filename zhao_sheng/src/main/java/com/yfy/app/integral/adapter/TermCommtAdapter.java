package com.yfy.app.integral.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.beans.TermCommt;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/4.
 */

public class TermCommtAdapter extends XlistAdapter<TermCommt> {

    public TermCommtAdapter(Context context, List<TermCommt> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<TermCommt> list) {
        TermCommt bean=list.get(position);
        holder.getView(TextView.class,R.id.term_commt_content).setText(bean.getContent());
        holder.getView(TextView.class,R.id.term_commt_title).setText(bean.getTitle());
        holder.getView(TextView.class,R.id.term_commt_time).setText(bean.getDate());
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.term_commt_item;
        resInfo.initIds=new int[]{
                R.id.term_commt_title,
                R.id.term_commt_time,
                R.id.term_commt_content,
        };
        return resInfo;
    }
}
