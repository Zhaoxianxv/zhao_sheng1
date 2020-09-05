package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.bean.AwardStuBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/4/10.
 */

public class AwardStuAdapter extends XlistAdapter<AwardStuBean> {

    private Context context;

    public AwardStuAdapter(Context context, List<AwardStuBean> list) {
        super(context, list);
        this.context=context;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<AwardStuBean> list) {
        AwardStuBean type=list.get(position);
        TextView txt=holder.getView(TextView.class,R.id.award_stu_txt_textview);
        txt.setText(type.getName());
        if (type.isChick()){
            holder.getView(ImageView.class,R.id.award_chick_iv).setVisibility(View.VISIBLE);
            txt.setTextColor(context.getResources().getColor(R.color.app_base_color));
        }else{
            holder.getView(ImageView.class,R.id.award_chick_iv).setVisibility(View.GONE);
            txt.setTextColor(context.getResources().getColor(R.color.app_base_text_color));
        }
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.award_stu_txt;
        resInfo.initIds=new int[]{
                R.id.award_stu_txt_textview,
                R.id.award_chick_iv,
        };
        return resInfo;
    }
}
