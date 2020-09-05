package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.bean.GradeBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/4/10.
 */

public class AwardClassTabAdapter extends XlistAdapter<GradeBean> {



    public AwardClassTabAdapter(Context context, List<GradeBean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<GradeBean> list) {
        GradeBean type=list.get(position);
        holder.getView(TextView.class,R.id.award_class_txt_textview).setText(type.getClass_name());
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.award_class_txt;
        resInfo.initIds=new int[]{
                R.id.award_class_txt_textview
        };
        return resInfo;
    }
}
