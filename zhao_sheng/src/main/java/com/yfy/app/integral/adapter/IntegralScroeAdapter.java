package com.yfy.app.integral.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.beans.IntegralScroe;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/14.
 */

public class IntegralScroeAdapter extends XlistAdapter<IntegralScroe> {


    private String textColor="#323232";
    private String numColor="#992429";


    public IntegralScroeAdapter(Context context, List<IntegralScroe> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<IntegralScroe> list) {
        IntegralScroe scroe=list.get(position);
        String num="  ";
        if (StringJudge.isNotEmpty(scroe.getScore())){
            String n=scroe.getScore().substring(0,1);
            if (n.equals("-")){
                num=num+scroe.getScore();
            }else{
                num=num+"+"+scroe.getScore();
            }
        }

        String msg= "<font color="+textColor+" >"+scroe.getType()+"</font>"+"<font color="+numColor+">"+num+"</font>";
        holder.getView(TextView.class,R.id.integral_list_sroe).setText(Html.fromHtml(msg));

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.integral_scroe_item;
        resInfo.initIds=new int[]{
                R.id.integral_list_sroe
        };
        return resInfo;
    }
}
