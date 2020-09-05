package com.yfy.app.integral.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.integral.beans.HonorStu;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/19.
 */

public class HonorTeaAdapter extends XlistAdapter<HonorStu> {


    public HonorTeaAdapter(Context context, List<HonorStu> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<HonorStu> list) {
        HonorStu stu=list.get(position);
        holder.getView(TextView.class,R.id.honor_tea_item_rank).setText(stu.getRank());
        holder.getView(TextView.class,R.id.honor_tea_item_name).setText(stu.getStuname());
        holder.getView(TextView.class,R.id.honor_tea_item_type).setText(stu.getType());
        holder.getView(TextView.class,R.id.honor_tea_item_add_time).setText("获奖日期："+stu.getAdddate().replace("/","-"));
        holder.getView(TextView.class,R.id.honor_tea_item_get_time).setText("上传日期："+stu.getRewarddate().replace("/","-"));
        holder.getView(TextView.class,R.id.honor_tea_item_content).setText(stu.getContent());
        ImageView pic=holder.getView(ImageView.class,R.id.honor_tea_item_pic);

        int width=pic.getMaxWidth();

        if (StringJudge.isEmpty(stu.getPic())){
            pic.setVisibility(View.GONE);
        }else{
            pic.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(stu.getPic())
                    .into(pic);
        }
    }

    @Override
    public ResInfo getResInfo() {

        ResInfo  resInfo=new ResInfo();
        resInfo.layout= R.layout.integral_honor_tea_itme;
        resInfo.initIds=new int[]{
                R.id.honor_tea_item_rank,
                R.id.honor_tea_item_add_time,
                R.id.honor_tea_item_get_time,
                R.id.honor_tea_item_content,
                R.id.honor_tea_item_pic,
                R.id.honor_tea_item_type,
                R.id.honor_tea_item_name,
        };
        return resInfo;
    }
}
