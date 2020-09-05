package com.yfy.app.cyclopedia.adpater;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.AncyclopediaList;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/10.
 */

public class TypeAdapter extends XlistAdapter<AncyclopediaList> {




    public TypeAdapter(Context context, List<AncyclopediaList> list) {
        super(context, list);
        this.context=context;

    }

    @Override
    public void renderData(int position, XlistAdapter<AncyclopediaList>.ViewHolder holder, List<AncyclopediaList> list) {
        AncyclopediaList bean=list.get(position);
        holder.getView(TextView.class,R.id.cyclopedia_type_tv).setText(bean.getTitle());
        holder.getView(TextView.class,R.id.cyclopedia_type_spoor_tv).setText(bean.getCount());
        holder.getView(TextView.class,R.id.cyclopedia_type_grade_tv).setText(bean.getTag_title());

        ImageView headIcon=holder.getView(ImageView.class,R.id.cyclopedia_type_iv);
        ImageView tagicon=holder.getView(ImageView.class,R.id.cyclopedia_type_two_iv);
        if (bean.getImages()!=null){
            GlideTools.chanCircle(context,bean.getImages(),headIcon,R.drawable.head_user);

        }


        if (bean.getKeyword().equals("自然")){
            Glide.with(context)
                    .load(R.drawable.cyc_tree)
                    .into(tagicon);
        }else{
            Glide.with(context)
                    .load(R.drawable.cyc_cultural)
                    .into(tagicon);
        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.cyclopedia_type_item;
        resInfo.initIds=new int[]{
                R.id.cyclopedia_type_tv,
                R.id.cyclopedia_type_iv,
                R.id.cyclopedia_type_two_iv,
                R.id.cyclopedia_type_grade_tv,
                R.id.cyclopedia_type_spoor_tv

        };
        return resInfo;
    }
}
