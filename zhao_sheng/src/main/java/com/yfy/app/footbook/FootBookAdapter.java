package com.yfy.app.footbook;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/1.
 */

public class FootBookAdapter extends XlistAdapter<Foot> {

    public FootBookAdapter(Context context, List<Foot> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<Foot> list) {
        Foot foot=list.get(position);
        final String id=foot.getId();
        final String isP=foot.getIspraise();
        holder.getView(TextView.class,R.id.foot_content).setText(foot.getContent());
        holder.getView(TextView.class,R.id.foot_title).setText(foot.getName());
        holder.getView(TextView.class,R.id.foot_reting).setText(foot.getPraise());
        ImageView icon=holder.getView(ImageView.class,R.id.foot_image);
        ImageView priase=holder.getView(ImageView.class,R.id.foot_ispriase);
        if (foot.getIspraise().equals("true")){
            GlideTools.loadImage(context, R.drawable.graded_dynamic_praise_selected, priase);
        }else{
            GlideTools.loadImage(context, R.drawable.graded_dynamic_praise_unselected, priase);
        }
        GlideTools.chanMult(context, foot.getImage(), icon);
        priase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPriase!=null){
                    isPriase.isPriase(id,isP);
                }
            }
        });


    }

    @Override
    public ResInfo getResInfo() {
        ResInfo infor=new ResInfo();
        infor.layout= R.layout.foot_book_item;
        infor.initIds=new int[]{
            R.id.foot_image,
            R.id.foot_title,
            R.id.foot_content,
            R.id.foot_reting,
            R.id.foot_ispriase,
        };
        return infor;
    }


    public IsPriase isPriase;

    public void setIsPriase(IsPriase isPriase) {
        this.isPriase = isPriase;
    }

    public interface IsPriase{
        void isPriase(String id,String state);
    }
}
