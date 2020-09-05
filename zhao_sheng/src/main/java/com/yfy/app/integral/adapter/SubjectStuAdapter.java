package com.yfy.app.integral.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.beans.SubjectStu;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/4.
 */

public class SubjectStuAdapter extends XlistAdapter<SubjectStu> {

    public SubjectStuAdapter(Context context, List<SubjectStu> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<SubjectStu> list) {
        SubjectStu bean=list.get(position);
        TextView name =holder.getView(TextView.class,R.id.subject_good_name);
        holder.getView(TextView.class,R.id.subject_good_user).setText(bean.getCoursename());
        if (StringJudge.isEmpty(bean.getTeachername())){
            name.setText(bean.getStuname());
        }else{
            name.setText(bean.getTeachername());
        }
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.term_csubject_item;
        resInfo.initIds=new int[]{
                R.id.subject_good_user,
                R.id.subject_good_name,
        };
        return resInfo;
    }
}
