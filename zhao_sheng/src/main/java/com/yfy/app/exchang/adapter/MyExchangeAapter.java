package com.yfy.app.exchang.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.exchang.bean.MyCouyseBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/28.
 */

public class MyExchangeAapter extends XlistAdapter<MyCouyseBean> {


    public MyExchangeAapter(Context context, List<MyCouyseBean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<MyCouyseBean> list) {
        MyCouyseBean bean=list.get(position);
        holder.getView(TextView.class,R.id.exchange_submit_1)
                .setText(bean.getDate().replace("/","-")+"("+bean.getNo()+")");
        holder.getView(TextView.class,R.id.exchange_submit_2)
                .setText(bean.getDate1().replace("/","-")+"("+bean.getNo1()+")");
        holder.getView(TextView.class,R.id.exchange_submit_3)
                .setText(bean.getCoursename()+"("+bean.getTeachername()+")");
        holder.getView(TextView.class,R.id.exchange_submit_4)
                .setText(bean.getCoursename1()+"("+bean.getTeachername1()+")");
        TextView text=holder.getView(TextView.class,R.id.exchange_tag_red_text);

        if (bean.getState().equals("待审核")){
            text.setVisibility(View.GONE);
        }else{
            text.setVisibility(View.VISIBLE);
            if (bean.getState().equals("通过")){
                text.setTextColor(context.getResources().getColor(R.color.green));
                text.setText(bean.getState());
                text.setBackgroundResource(R.drawable.oval_line1_green);
            }else if (bean.getState().equals("拒绝")){
                text.setText(bean.getState());
                text.setTextColor(context.getResources().getColor(R.color.red));
                text.setBackgroundResource(R.drawable.oval_line1_red);
            }else{
                text.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo infor=new ResInfo();
        infor.layout= R.layout.exchange_my_item;
        infor.initIds=new int[]{
            R.id.exchange_submit_1,
            R.id.exchange_submit_2,
            R.id.exchange_submit_3,
            R.id.exchange_submit_4,
            R.id.exchange_tag_red_text,
        };
        return infor;
    }
}
