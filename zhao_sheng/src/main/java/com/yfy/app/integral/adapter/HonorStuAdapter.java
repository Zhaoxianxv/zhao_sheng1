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

public class HonorStuAdapter extends XlistAdapter<HonorStu> {


    private Context context;
    public HonorStuAdapter(Context context, List<HonorStu> list) {
        super(context, list);
        this.context=context;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<HonorStu> list) {
        final HonorStu stu=list.get(position);
        holder.getView(TextView.class,R.id.honor_stu_item_rank).setText("获奖级别："+stu.getRank());

        holder.getView(TextView.class,R.id.honor_stu_item_type).setText(stu.getType());
        holder.getView(TextView.class,R.id.honor_stu_item_get_time).setText("获奖日期："+stu.getRewarddate().replace("/","-"));
        holder.getView(TextView.class,R.id.honor_stu_item_add_time).setText("获奖单位："+stu.getOrg());
        holder.getView(TextView.class,R.id.honor_stu_item_content).setText(stu.getContent());
        ImageView pic=holder.getView(ImageView.class,R.id.honor_stu_item_pic);

        if (StringJudge.isEmpty(stu.getPic())){
            pic.setVisibility(View.GONE);
        }else{
            pic.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(stu.getPic())
                    .into(pic);

        }

        holder.getView(TextView.class,R.id.honor_stu_item_clear)
                 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClear!=null){
                    itemClear.itemClear(stu);
                }
            }
        });

    }

    @Override
    public ResInfo getResInfo() {

        ResInfo  resInfo=new ResInfo();
        resInfo.layout= R.layout.integral_honor_stu_itme;
        resInfo.initIds=new int[]{
                R.id.honor_stu_item_rank,
                R.id.honor_stu_item_add_time,
                R.id.honor_stu_item_get_time,
                R.id.honor_stu_item_content,
                R.id.honor_stu_item_pic,
                R.id.honor_stu_item_type,
                R.id.honor_stu_item_clear,

        };
        return resInfo;
    }
    public ItemOnclick itemClear;

    public void setItemClear(ItemOnclick itemClear) {
        this.itemClear = itemClear;
    }

    public interface ItemOnclick{
        void itemClear(HonorStu stu);
    }
}
