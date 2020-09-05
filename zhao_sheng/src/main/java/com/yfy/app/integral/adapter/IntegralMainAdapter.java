package com.yfy.app.integral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.TagFinal;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/6.
 */

public class IntegralMainAdapter extends XlistAdapter<String> {


    private int[] ids;
    private int[] stu_id=new int[]{
                    R.drawable.integral_1,
                    R.drawable.integral_2,
                    R.drawable.integral_3,

                    R.drawable.integral_4,
                    R.drawable.integral_6,
                    R.drawable.integral_5,
                    R.drawable.integral_3,
              };
    private int[] tea_id=new int[]{
                    R.drawable.integral_7,
                    R.drawable.integral_3,
                    R.drawable.integral_1,
                    R.drawable.integral_6,
                    R.drawable.integral_4,
                    R.drawable.integral_5,
              };
    private Context context;


    public IntegralMainAdapter(Context context,List<String> list) {
        super(context,list);
        this.context=context;
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            ids=tea_id;
        }else{
            ids=stu_id;
        }
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<String> list) {
        String item=list.get(position);
        holder.getView(TextView.class, R.id.integral_main_item_name).setText(item);
        final ImageView image=holder.getView(ImageView.class, R.id.integral_main_item_image);
        if (ids.length>position){
            Glide.with(context).load(ids[position])
                    .into(image);
        }else{
            Glide.with(context).load(ids[0])
                    .into(image);
            return;
        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo res=new ResInfo();
        res.layout= R.layout.integral_main_item;
        res.initIds=new int[]{
            R.id.integral_main_item_image,
            R.id.integral_main_item_name,
        };
        return res;
    }
}
